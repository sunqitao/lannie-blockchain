package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.block.check.CheckerManager
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.RpcBlockBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.pbft.VoteType
import cn.lannie.kt.blockchain.socket.pbft.msg.VotePreMsg
import cn.lannie.kt.blockchain.socket.pbft.queue.MsgQueueManager
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext

/**
 * 收到请求生成区块消息，进入PrePre队列
 *
 */
class GenerateBlockRequestHandler : AbstractBlockHandler<RpcBlockBody>() {
    private val logger = LoggerFactory.getLogger(GenerateBlockRequestHandler::class.java)


    override fun bodyClass(): Class<RpcBlockBody> {
        return RpcBlockBody::class.java
    }

    override fun handler(packet: BlockPacket, rpcBlockBody: RpcBlockBody, channelContext: ChannelContext): Any? {
        val block = rpcBlockBody.block
        logger.info("收到来自于<" + rpcBlockBody.appId + "><请求生成Block>消息，block信息为[" + block + "]")

        val checkerManager = ApplicationContextProvider.getBean(CheckerManager::class.java)
        //对区块的基本信息进行校验，校验通过后进入pbft的Pre队列
        val rpcCheckBlockBody = checkerManager.check(block!!)
        logger.info("校验结果:" + rpcCheckBlockBody.toString())
        if (rpcCheckBlockBody.code === 0) {
            val votePreMsg = VotePreMsg()
            votePreMsg.block = block
            votePreMsg.voteType = VoteType.PREPREPARE
            votePreMsg.number = (block!!.blockHeader!!.number)
            votePreMsg.appId = (rpcBlockBody.appId)
            votePreMsg.hash = (block.hash)
            votePreMsg.isAgree = (true)
            //将消息推入PrePrepare队列
            ApplicationContextProvider.getBean(MsgQueueManager::class.java).pushMsg(votePreMsg)
        }

        return null
    }
}
