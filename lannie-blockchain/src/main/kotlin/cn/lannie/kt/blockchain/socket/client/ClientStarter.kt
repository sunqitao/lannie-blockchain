package cn.lannie.kt.blockchain.socket.client

import cn.lannie.kt.blockchain.common.AppId
import cn.lannie.kt.blockchain.common.CommonUtil
import cn.lannie.kt.blockchain.core.bean.MemberData
import cn.lannie.kt.blockchain.core.bean.PermissionData
import cn.lannie.kt.blockchain.core.event.NodesConnectedEvent
import cn.lannie.kt.blockchain.core.manager.PermissionManager
import cn.lannie.kt.blockchain.socket.common.Const
import cn.lannie.kt.blockchain.socket.common.Const.Companion.GROUP_NAME
import cn.lannie.kt.blockchain.socket.packet.NextBlockPacketBuilder
import com.google.common.collect.Maps
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.tio.client.AioClient
import org.tio.client.ClientGroupContext
import org.tio.core.Aio
import org.tio.core.ChannelContext
import org.tio.core.Node
import java.util.HashSet
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import javax.annotation.Resource


/**
 */
@Component
class ClientStarter {
    @Resource
    private val clientGroupContext: ClientGroupContext? = null
    @Resource
    private val packetSender: PacketSender? = null
    @Resource
    private val restTemplate: RestTemplate? = null
    @Resource
    private val permissionManager: PermissionManager? = null
    @Value("\${managerUrl}")
    private val managerUrl: String? = null
    @Value("\${appId}")
    private val appId: String? = null
    @Value("\${name}")
    private val name: String? = null
    @Value("\${singleNode:false}")
    private val singleNode: Boolean? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    private val nodes = HashSet<Node>()

    // 节点连接状态
    private val nodesStatus = Maps.newConcurrentMap<String, Int>()
    @Volatile
    private var isNodesReady = false // 节点是否已准备好

    /**
     * 初始化权限信息
     * 避免新联盟节点加入时，同步区块而权限未初始化导致同步异常
     */
    @PostConstruct
    fun initPermission() {
        fetchPermission()
    }

    /**
     * 从麦达区块链管理端获取已登记的各服务器ip
     * 隔5分钟去获取一次
     */
    @Scheduled(fixedRate = 300000)
    fun fetchOtherServer() {
        val localIp = CommonUtil().localIp
        logger.info("本机IP：{}", localIp)
        try {
            //如果连不上服务器，就不让启动
            val memberData = restTemplate?.getForEntity(managerUrl + "member?name=" + name + "&appId=" + AppId
                    .value +
                    "&ip=" +
                    localIp,
                    MemberData::class.java)?.getBody()
            //合法的客户端
            if (memberData!!.code === 0) {
                val memberList = memberData!!.members
                logger.info("共有" + memberList!!.size + "个成员需要连接：" + memberList.toString())

                nodes.clear()
                for (member in memberList) {
                    val node = Node(member.ip, Const.PORT)
                    nodes.add(node)
                }
                //开始尝试绑定到对方开启的server
                bindServerGroup(nodes)

            } else {
                logger.error("不是合法有效的已注册的客户端")
                System.exit(0)
            }
        } catch (e: Exception) {
            logger.error("请先启动md_blockchain_manager服务，并配置appId等属性，只有合法联盟链成员才能启动该服务",e)
            System.exit(0)
        }

    }

    /**
     * 从麦达区块链管理端获取权限信息，一天获取一次即可
     */
    @Scheduled(fixedRate = (1000 * 60 * 60 * 24).toLong(), initialDelay = 2000)
    fun fetchPermission() {
        try {
            //如果连不上服务器，就不让启动
            val permissionData = restTemplate?.getForEntity(managerUrl + "permission?name=" + name,
                    PermissionData::class.java)?.getBody()
            //获取到权限
            if (permissionData!!.code === 0) {
                val permissionList = permissionData?.permissions
                permissionManager?.savePermissionList(permissionList!!)
            } else {
                logger.error("无法获取权限信息")
                System.exit(0)
            }
        } catch (e: Exception) {
            logger.error("请先启动md_blockchain_manager服务，并配置appId等属性，只有合法联盟链成员才能启动该服务",e)
            System.exit(0)
        }

    }

    /**
     * 每30秒群发一次消息，和别人对比最新的Block
     */
    @Scheduled(fixedRate = 30000)
    fun heartBeat() {
        if (!isNodesReady) {
            return
        }
        logger.info("---------开始心跳包--------")
        val blockPacket = NextBlockPacketBuilder.build()
        packetSender!!.sendGroup(blockPacket)
    }

    fun onNodesReady() {
        logger.info("开始群发信息获取next Block")
        //在这里发请求，去获取group别人的新区块
        val nextBlockPacket = NextBlockPacketBuilder.build()
        packetSender!!.sendGroup(nextBlockPacket)
    }

    /**
     * client在此绑定多个服务器，多个服务器为一个group，将来发消息时发给一个group。
     * 此处连接的server的ip需要和服务器端保持一致，服务器删了，这边也要踢出Group
     */
    private fun bindServerGroup(serverNodes: Set<Node>) {
        //当前已经连接的
        val setWithLock = Aio.getAllChannelContexts(clientGroupContext!!)
        val lock2 = setWithLock.lock.readLock()
        lock2.lock()
        try {
            val set = setWithLock.obj
            //已连接的节点集合
            val connectedNodes = set.stream().map {
                it.serverNode
            }.collect(Collectors.toSet())
//                    set.stream().map<Node>(Function<ChannelContext, Node> { it.getServerNode() }).collect<Set<Node>, Any>(Collectors.toSet())

            //连接新增的，删掉已在管理端不存在的
            for (node in serverNodes) {
                if (!connectedNodes.contains(node)) {
                    connect(node)
                }
            }
            //删掉已经不存在
            for (channelContext in set) {
                val node = channelContext.serverNode
                if (!serverNodes.contains(node)) {
                    Aio.remove(channelContext, "主动关闭" + node.ip)
                }

            }
        } finally {
            lock2.unlock()
        }

    }

    private fun connect(serverNode: Node) {
        try {
            val aioClient = AioClient(clientGroupContext!!)
            logger.info("开始绑定:$serverNode")
            aioClient.asynConnect(serverNode)
        } catch (e: Exception) {
            logger.info("异常")
        }

    }

    @EventListener(NodesConnectedEvent::class)
    fun onConnected(connectedEvent: NodesConnectedEvent) {
        val channelContext = connectedEvent.getSource()
        val node = channelContext.getServerNode()
        if (channelContext.isClosed()) {
            logger.info("连接" + node.toString() + "失败")
            nodesStatus[node.getIp()] = -1
            return
        } else {
            logger.info("连接" + node.toString() + "成功")
            nodesStatus[node.getIp()] = 1
            //绑group是将要连接的各个服务器节点做为一个group
            Aio.bindGroup(channelContext, GROUP_NAME)

            val csize = Aio.getAllChannelContexts(clientGroupContext!!).size()
            if (csize >= pbftAgreeCount()) {
                synchronized(nodesStatus) {
                    if (!isNodesReady) {
                        isNodesReady = true
                        onNodesReady()
                    }
                }
            }
        }
    }

    fun halfGroupSize(): Int {
        val setWithLock = clientGroupContext!!.groups.clients(clientGroupContext, Const.GROUP_NAME)
        return setWithLock.obj.size / 2
    }

    /**
     * pbft算法中拜占庭节点数量f，总节点数3f+1
     *
     * @return f
     */
    fun pbftSize(): Int {
        //Group内共有多少个节点
        val total = nodes.size
        var pbft = (total - 1) / 3
        if (pbft <= 0) {
            pbft = 1
        }
        //如果要单节点测试，此处返回值改为0
        return if (singleNode!!) {
            0
        } else pbft
    }

    fun pbftAgreeCount(): Int {
        return pbftSize() * 2 + 1
    }
}
