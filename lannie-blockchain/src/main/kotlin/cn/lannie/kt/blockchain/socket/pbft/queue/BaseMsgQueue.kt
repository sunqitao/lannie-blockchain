package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.lannie.kt.blockchain.socket.client.ClientStarter
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.springframework.stereotype.Component
import javax.annotation.Resource


/**
 * 各节点互传的投票消息存储队列基类
 *
 */
@Component
abstract class BaseMsgQueue {
    @Resource
    private val clientStarter: ClientStarter? = null

    fun pbftSize(): Int {
        return clientStarter!!.pbftSize()
    }

    fun pbftAgreeSize(): Int {
        return clientStarter!!.pbftAgreeCount()
    }

    /**
     * 来了新消息
     *
     * @param voteMsg
     * voteMsg
     */
    abstract fun push(voteMsg: VoteMsg)
}
