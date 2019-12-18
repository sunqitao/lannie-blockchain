package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.pbft.VoteType
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.springframework.stereotype.Component


/**
 */
@Component
class MsgQueueManager {

    fun pushMsg(voteMsg: VoteMsg) {
        var baseMsgQueue: BaseMsgQueue? = null
        when (voteMsg.voteType) {
            VoteType
                    .PREPREPARE -> baseMsgQueue = ApplicationContextProvider.getBean(PreMsgQueue::class.java)
            VoteType.PREPARE -> baseMsgQueue = ApplicationContextProvider.getBean(PrepareMsgQueue::class.java)
            VoteType.COMMIT -> baseMsgQueue = ApplicationContextProvider.getBean(CommitMsgQueue::class.java)
            else -> {
            }
        }
        baseMsgQueue?.push(voteMsg)
    }
}
