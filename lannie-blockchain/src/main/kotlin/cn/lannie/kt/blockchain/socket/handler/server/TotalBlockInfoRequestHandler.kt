package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.RpcBlockBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext
import org.tio.utils.json.Json

/**
 * 获取全部区块信息的请求，全网广播
 */
class TotalBlockInfoRequestHandler : AbstractBlockHandler<RpcBlockBody>() {
    private val logger = LoggerFactory.getLogger(TotalBlockInfoRequestHandler::class.java)

    override fun bodyClass(): Class<RpcBlockBody> {
        return RpcBlockBody::class.java
    }

    @Throws(Exception::class)
    override fun handler(packet: BlockPacket, rpcBlockBody: RpcBlockBody, channelContext: ChannelContext): Any? {
        logger.info("收到<请求生成Block的回应>消息", Json.toJson(rpcBlockBody))

        //TODO check合法性
        //TODO response

        return null
    }
}
