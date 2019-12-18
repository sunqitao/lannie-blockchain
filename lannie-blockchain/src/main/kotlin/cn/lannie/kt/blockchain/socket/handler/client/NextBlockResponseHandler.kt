package cn.lannie.kt.blockchain.socket.handler.client

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.BlockHash
import cn.lannie.kt.blockchain.socket.body.RpcNextBlockBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.pbft.queue.NextBlockQueue
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext

/**
 * 对方根据我们传的hash，给我们返回的next block
 *
 */
class NextBlockResponseHandler : AbstractBlockHandler<RpcNextBlockBody>() {
    private val logger = LoggerFactory.getLogger(TotalBlockInfoResponseHandler::class.java!!)

    override fun bodyClass(): Class<RpcNextBlockBody> {
        return RpcNextBlockBody::class.java
    }

    override fun handler(packet: BlockPacket, rpcBlockBody: RpcNextBlockBody, channelContext: ChannelContext): Any? {
        logger.info("收到来自于<" + rpcBlockBody.appId + ">的回复，下一个Block hash为：" + rpcBlockBody.hash)

        val hash = rpcBlockBody.hash
        //如果为null，说明对方根据我们传过去的hash，找不到next block。说明要么已经是最新了，要么对方的block比自己的少
        if (hash == null) {
            logger.info("和<" + rpcBlockBody.appId + ">相比，本地已是最新块了")
        } else {
            val blockHash = BlockHash(hash, rpcBlockBody.prevHash!!, rpcBlockBody.appId!!)
            //此处进行搜集next block的hash，相同的hash过2f+1时可以确认
            ApplicationContextProvider.getBean(NextBlockQueue::class.java).push(blockHash)
        }

        return null
    }
}
