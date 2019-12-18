package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.hutool.core.util.StrUtil
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.socket.body.BlockHash
import cn.lannie.kt.blockchain.socket.body.RpcSimpleBlockBody
import cn.lannie.kt.blockchain.socket.client.ClientStarter
import cn.lannie.kt.blockchain.socket.client.PacketSender
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors
import javax.annotation.Resource


/**
 * 处理请求next block时的返回
 *
 */
@Component
class NextBlockQueue {
    @Resource
    private val dbBlockManager: DbBlockManager? = null
    @Resource
    private val clientStarter: ClientStarter? = null
    @Resource
    private val packetSender: PacketSender? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * prevHash->hash，记录上一区块hash和hash的映射
     */
    private val requestMap = ConcurrentHashMap<String, List<BlockHash>>()

    /**
     * 保存已经通过的区块hash,用于后面校验落地区块
     */
    private val wantHashs = Lists.newCopyOnWriteArrayList<String>()

    fun pop(hash: String): String? {
        return if (wantHashs.remove(hash)) {
            hash
        } else null
    }

    operator fun get(key: String): List<BlockHash>? {
        return requestMap[key]
    }

    fun put(key: String, responses: List<BlockHash>) {
        requestMap[key] = responses
    }

    private fun add(key: String, blockHash: BlockHash) {
        var baseResponses: MutableList<BlockHash>? = get(key) as MutableList<BlockHash>?

        if (baseResponses == null) {
            baseResponses = ArrayList<BlockHash>()
        }
        //避免同一个机构多次投票
        for (oldResponse in baseResponses!!) {
            if (StrUtil.equals(oldResponse.appId, blockHash.appId)) {
                return
            }
        }
        baseResponses.add(blockHash)
        put(key, baseResponses)
    }

    /**
     * 查询key对应的BlockHash集合中，hash相同的数量
     *
     * @param key
     * key
     * @return hash最多的集合
     */
    fun findMaxHash(key: String): List<BlockHash> {
        val blockHashes = get(key)
        //寻找hash相同的总数量
        val map = HashMap<String, Int>()
        blockHashes!!.forEach {
            val hash = it.hash
            (map as java.util.Map<String, Int>).merge(hash!!, 1) { a, b -> a + b }
        }
        //找到value最大的那个key，即被同意最多的hash
        val hash = getMaxKey(map)
        return blockHashes.stream().filter { blockHash -> hash == blockHash.hash }.collect(Collectors.toList<BlockHash>())
    }

    private fun getMaxKey(hashMap: Map<String, Int>): String? {
        var value: Int
        var flagValue = 0
        var key: String
        var flagKey: String? = null
        val entrySet = hashMap.entries
        for ((key1, value1) in entrySet) {
            key = key1
            value = value1

            if (flagValue < value) {
                //flagKey flagValue 当判断出最大值是将最大值赋予该变量
                flagKey = key
                flagValue = value
            }
        }
        return flagKey
    }


    fun remove(key: String) {
        requestMap.remove(key)
    }

    /**
     * 群发请求nextBlock的请求，收到新的回复，在此做处理。
     *
     * @param blockHash
     * blockHash
     */
    fun push(blockHash: BlockHash) {
        val wantHash = blockHash.hash
        var prevHash = blockHash.prevHash
        //创世块
        if (prevHash == null) {
            prevHash = "first_block_hash"
        }
        //针对该hash已经处理过了
        if (dbBlockManager!!.getBlockByHash(wantHash!!) != null) {
            remove(prevHash)
            return
        }
        add(prevHash, blockHash)

        val agreeCount = clientStarter!!.pbftAgreeCount()

        //寻找集合中，哪个hash数最多
        val maxCount = findMaxHash(prevHash).size

        //判断数量是否过线
        if (maxCount >= agreeCount - 1) {
            logger.info("共有<$maxCount>个节点返回next block hash为$wantHash")
            wantHashs.add(wantHash)
            //请求拉取该hash的Block
            val blockPacket = PacketBuilder<RpcSimpleBlockBody>().setType(PacketType
                    .FETCH_BLOCK_INFO_REQUEST).setBody(RpcSimpleBlockBody(wantHash)).build()
            packetSender!!.sendGroup(blockPacket)
            //remove后，这一次请求内的后续回复就凑不够agreeCount了，就不会再次触发全员请求block
            remove(prevHash)
        }

    }

}
