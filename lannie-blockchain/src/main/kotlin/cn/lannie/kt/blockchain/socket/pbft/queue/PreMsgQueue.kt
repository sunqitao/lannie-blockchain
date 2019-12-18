package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.hutool.core.bean.BeanUtil
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.common.AppId
import cn.lannie.kt.blockchain.common.timer.TimerManager
import cn.lannie.kt.blockchain.core.event.AddBlockEvent
import cn.lannie.kt.blockchain.core.sqlite.SqliteManager
import cn.lannie.kt.blockchain.socket.pbft.VoteType
import cn.lannie.kt.blockchain.socket.pbft.event.MsgPrepareEvent
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import cn.lannie.kt.blockchain.socket.pbft.msg.VotePreMsg
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier
import javax.annotation.Resource


/**
 * preprepare消息的存储，但凡收到请求生成Block的信息，都在这里处理
 *
 */
@Component
class PreMsgQueue : BaseMsgQueue() {
    @Resource
    private val sqliteManager: SqliteManager? = null
    @Resource
    private val prepareMsgQueue: PrepareMsgQueue? = null
    @Resource
    private val eventPublisher: ApplicationEventPublisher? = null

    private val blockConcurrentHashMap = ConcurrentHashMap<String, VotePreMsg>()

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun push(voteMsg: VoteMsg) {
        //该队列里的是votePreMsg
        val votePreMsg = voteMsg as VotePreMsg
        val hash = votePreMsg.hash
        //避免收到重复消息
        if (blockConcurrentHashMap.get(hash) != null) {
            return
        }
        //但凡是能进到该push方法的，都是通过基本校验的，但在并发情况下可能会相同number的block都进到投票队列中
        //需要对新进来的Vote信息的number进行校验，如果在比prepre阶段靠后的阶段中，已经出现了认证OK的同number的vote，则拒绝进入该队列
        if (prepareMsgQueue!!.otherConfirm(hash!!, voteMsg.number)) {
            logger.info("拒绝进入Prepare阶段，hash为$hash")
            return
        }
        // 检测脚本是否正常
        try {
            sqliteManager!!.tryExecute(votePreMsg.block!!)
        } catch (e: Exception) {
            // 执行异常
            logger.info("sql指令预执行失败",e)
            return
        }

        //存入Pre集合中
        blockConcurrentHashMap.put(hash, votePreMsg)

        //加入Prepare行列，推送给所有人
        val prepareMsg = VoteMsg()
        BeanUtil.copyProperties(voteMsg, prepareMsg)
        prepareMsg.voteType = (VoteType.PREPARE)
        prepareMsg.appId = (AppId.value)
        eventPublisher!!.publishEvent(MsgPrepareEvent(prepareMsg))
    }

    /**
     * 根据hash，得到内存中的Block信息
     *
     * @param hash
     * hash
     * @return Block
     */
    fun findByHash(hash: String): Block? {
        val votePreMsg = blockConcurrentHashMap.get(hash)
        return if (votePreMsg != null) {
            votePreMsg!!.block
        } else null
    }

    /**
     * 新区块生成后，clear掉map中number比区块小的所有数据
     */
    @Order(3)
    @EventListener(AddBlockEvent::class)
    fun blockGenerated(addBlockEvent: AddBlockEvent) {
        val block = addBlockEvent.getSource() as Block
        val number = block.blockHeader?.number!!
        TimerManager().schedule(Supplier{
            for (key in blockConcurrentHashMap.keys) {
                if (blockConcurrentHashMap.get(key)?.number!! <= number) {
                    blockConcurrentHashMap.remove(key)
                }
            }
            null
        }, 2000)
    }
}
