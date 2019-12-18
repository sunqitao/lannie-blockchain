package cn.lannie.kt.blockchain.socket.packet

import cn.lannie.kt.blockchain.socket.body.BaseBody
import org.tio.utils.json.Json

/**
 */
class PacketBuilder<T : BaseBody> {
    /**
     * 消息类型，其值在Type中定义
     */
    private var type: Byte = 0

    private var body: T? = null

    fun setType(type: Byte): PacketBuilder<T> {
        this.type = type
        return this
    }

    fun setBody(body: T): PacketBuilder<T> {
        this.body = body
        return this
    }

    fun build(): BlockPacket {
        return BlockPacket(type, Json.toJson(body))
    }
}
