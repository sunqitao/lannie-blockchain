package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.HeartBeatBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext

/**
 * 客户端心跳包
 */
@Deprecated("")
class HeartBeatHandler : AbstractBlockHandler<HeartBeatBody>() {
    private val logger = LoggerFactory.getLogger(HeartBeatHandler::class.java)

    override fun bodyClass(): Class<HeartBeatBody> {
        return HeartBeatBody::class.java
    }

    @Throws(Exception::class)
    override fun handler(packet: BlockPacket, heartBeatBody: HeartBeatBody, channelContext: ChannelContext): Any? {
        logger.info("收到<心跳包>消息", heartBeatBody.text)

        return null
    }
}
