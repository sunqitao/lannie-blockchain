package cn.lannie.kt.blockchain.socket.distruptor.base

import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.tio.core.ChannelContext
import java.io.Serializable

/**
 * 生产、消费者之间传递消息用的event
 *
 */
class BaseEvent : Serializable {
    var blockPacket: BlockPacket? = null
    var channelContext: ChannelContext? = null

    constructor(blockPacket: BlockPacket, channelContext: ChannelContext) {
        this.blockPacket = blockPacket
        this.channelContext = channelContext
    }

    constructor(blockPacket: BlockPacket) {
        this.blockPacket = blockPacket
    }

    constructor() {}
}
