package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.core.event.AddBlockEvent
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * Confirm阶段的消息队列
 * 每个节点收到超过2f+1个不同节点（包括自己）的commit消息后，就认为该区块已经达成一致，进入committed状态，并将其持久化到区块链数据库中
 *
 */
@Component
class CommitMsgQueue : AbstractVoteMsgQueue() {
    @Resource
    private val preMsgQueue: PreMsgQueue? = null

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun deal(voteMsg: VoteMsg, voteMsgs: List<VoteMsg>) {
        val hash = voteMsg.hash

        //通过校验agree数量，来决定是否在本地生成Block
        val count = voteMsgs.stream().map { it.isAgree }.count()
//                filter(Predicate<VoteMsg> { VoteMsg.isAgree }).count()
        logger.info("已经commit为true的数量为:$count")
        if (count >= pbftAgreeSize()) {
            val block = preMsgQueue!!.findByHash(hash!!) ?: return
//本地落地
            voteStateConcurrentHashMap[hash] = true
            ApplicationContextProvider.publishEvent(AddBlockEvent(block))
        }
    }

    /**
     * 新区块生成后，clear掉map中number比区块小的所有数据
     */
    @Order(3)
    @EventListener(AddBlockEvent::class)
    fun blockGenerated(addBlockEvent: AddBlockEvent) {
        val block = addBlockEvent.getSource() as Block
        clearOldBlockHash(block.blockHeader?.number!!)
    }

}
