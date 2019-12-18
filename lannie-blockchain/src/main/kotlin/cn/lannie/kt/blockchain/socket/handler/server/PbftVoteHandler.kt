package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.VoteBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.pbft.queue.MsgQueueManager
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext


/**
 * pbft投票处理
 *
 */
class PbftVoteHandler : AbstractBlockHandler<VoteBody>() {
    private val logger = LoggerFactory.getLogger(PbftVoteHandler::class.java)


    override fun bodyClass(): Class<VoteBody> {
        return VoteBody::class.java
    }

    override fun handler(packet: BlockPacket, voteBody: VoteBody, channelContext: ChannelContext): Any? {
        val voteMsg = voteBody.voteMsg
        logger.info("收到来自于<" + voteMsg!!.appId + "><投票>消息，投票信息为[" + voteMsg + "]")

        ApplicationContextProvider.getBean(MsgQueueManager::class.java).pushMsg(voteMsg)
        return null
    }
}
