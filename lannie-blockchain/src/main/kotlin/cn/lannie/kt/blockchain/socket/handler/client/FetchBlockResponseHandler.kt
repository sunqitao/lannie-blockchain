package cn.lannie.kt.blockchain.socket.handler.client

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.block.check.CheckerManager
import cn.lannie.kt.blockchain.core.event.AddBlockEvent
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.RpcBlockBody
import cn.lannie.kt.blockchain.socket.client.PacketSender
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.packet.NextBlockPacketBuilder
import cn.lannie.kt.blockchain.socket.pbft.queue.NextBlockQueue
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext
import org.tio.utils.json.Json

/**
 * 对方根据我们传的hash，给我们返回的block
 *
 */
class FetchBlockResponseHandler : AbstractBlockHandler<RpcBlockBody>() {
    private val logger = LoggerFactory.getLogger(TotalBlockInfoResponseHandler::class.java!!)

    override fun bodyClass(): Class<RpcBlockBody> {
        return RpcBlockBody::class.java
    }

    override fun handler(packet: BlockPacket, rpcBlockBody: RpcBlockBody, channelContext: ChannelContext): Any? {
        logger.info("收到来自于<" + rpcBlockBody.appId + ">的回复，Block为：" + Json.toJson(rpcBlockBody))

        val block = rpcBlockBody.block
        //如果为null，说明对方也没有该Block
        if (block == null) {
            logger.info("对方也没有该Block")
        } else {
            //此处校验传过来的block的合法性，如果合法，则更新到本地，作为next区块
            if (ApplicationContextProvider.getBean(NextBlockQueue::class.java).pop(block!!.hash!!) == null) return null

            val checkerManager = ApplicationContextProvider.getBean(CheckerManager::class.java)
            val rpcCheckBlockBody = checkerManager.check(block)
            //校验通过，则存入本地DB，保存新区块
            if (rpcCheckBlockBody.code === 0) {
                ApplicationContextProvider.publishEvent(AddBlockEvent(block))
                //继续请求下一块
                val blockPacket = NextBlockPacketBuilder.build()
                ApplicationContextProvider.getBean(PacketSender::class.java).sendGroup(blockPacket)
            }
        }

        return null
    }
}
