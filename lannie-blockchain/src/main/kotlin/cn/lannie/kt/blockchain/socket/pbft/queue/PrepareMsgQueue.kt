package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.hutool.core.bean.BeanUtil
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.common.AppId
import cn.lannie.kt.blockchain.core.event.AddBlockEvent
import cn.lannie.kt.blockchain.socket.pbft.VoteType
import cn.lannie.kt.blockchain.socket.pbft.event.MsgCommitEvent
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.function.Predicate
import javax.annotation.Resource


/**
 * Prepare阶段的消息队列
 *
 */
@Component
class PrepareMsgQueue : AbstractVoteMsgQueue() {
    @Resource
    private val commitMsgQueue: CommitMsgQueue? = null
    @Resource
    private val eventPublisher: ApplicationEventPublisher? = null
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 收到节点（包括自己）针对某Block的Prepare消息
     *
     * @param voteMsg
     * voteMsg
     */
    override fun deal(voteMsg: VoteMsg, voteMsgs: List<VoteMsg>) {
        val hash = voteMsg.hash
        val commitMsg = VoteMsg()
        BeanUtil.copyProperties(voteMsg, commitMsg)
        commitMsg.voteType = (VoteType.COMMIT)
        commitMsg.appId = (AppId.value)
        //开始校验并决定是否进入commit阶段
        //校验该vote是否合法
        if (commitMsgQueue!!.hasOtherConfirm(hash!!, voteMsg.number)) {
            agree(commitMsg, false)
        } else {
            //开始校验拜占庭数量，如果agree的超过2f + 1，就commit
            val agreeCount = voteMsgs.stream().map { it.isAgree }.count()
            val unAgreeCount = voteMsgs.size - agreeCount

            //开始发出commit的同意or拒绝的消息
            if (agreeCount >= pbftAgreeSize()) {
                agree(commitMsg, true)
            } else if (unAgreeCount >= pbftSize() + 1) {
                agree(commitMsg, false)
            }
        }

    }

    private fun agree(commitMsg: VoteMsg, flag: Boolean) {
        logger.info("Prepare阶段完毕，是否进入commit的标志是：$flag")
        //发出拒绝commit的消息
        commitMsg.isAgree = (flag)
        voteStateConcurrentHashMap[commitMsg.hash!!] = flag
        eventPublisher!!.publishEvent(MsgCommitEvent(commitMsg))
    }

    /**
     * 判断大家是否已对其他的Block达成共识，如果true，则拒绝即将进入队列的Block
     *
     * @param hash
     * hash
     * @return 是否存在
     */
    fun otherConfirm(hash: String, number: Int): Boolean {
        return if (commitMsgQueue!!.hasOtherConfirm(hash, number)) {
            true
        } else hasOtherConfirm(hash, number)
    }

    /**
     * 新区块生成后，clear掉map中number比区块小的所有数据
     *
     * @param addBlockEvent  addBlockEvent
     */
    @Order(3)
    @EventListener(AddBlockEvent::class)
    fun blockGenerated(addBlockEvent: AddBlockEvent) {
        val block = addBlockEvent.getSource() as Block
        clearOldBlockHash(block.blockHeader?.number!!)
    }
}
