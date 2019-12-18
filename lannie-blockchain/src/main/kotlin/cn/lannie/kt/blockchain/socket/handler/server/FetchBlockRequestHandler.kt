package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.RpcBlockBody
import cn.lannie.kt.blockchain.socket.body.RpcSimpleBlockBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.slf4j.LoggerFactory
import org.tio.core.Aio
import org.tio.core.ChannelContext

/**
 * 请求别人某个区块的信息
 */
class FetchBlockRequestHandler : AbstractBlockHandler<RpcSimpleBlockBody>() {
    private val logger = LoggerFactory.getLogger(FetchBlockRequestHandler::class.java)

    override fun bodyClass(): Class<RpcSimpleBlockBody> {
        return RpcSimpleBlockBody::class.java
    }

    override fun handler(packet: BlockPacket, rpcBlockBody: RpcSimpleBlockBody, channelContext: ChannelContext): Any? {
        logger.info("收到来自于<" + rpcBlockBody.appId+ "><请求该Block>消息，block hash为[" + rpcBlockBody.hash + "]")
        val block = ApplicationContextProvider.getBean(DbBlockManager::class.java).getBlockByHash(rpcBlockBody.hash!!)

        val blockPacket = PacketBuilder<RpcBlockBody>().setType(PacketType.FETCH_BLOCK_INFO_RESPONSE).setBody(RpcBlockBody(block)).build()
        Aio.send(channelContext, blockPacket)

        return null
    }
}
