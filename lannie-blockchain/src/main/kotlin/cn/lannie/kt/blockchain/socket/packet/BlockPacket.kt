package cn.lannie.kt.blockchain.socket.packet

import cn.lannie.kt.blockchain.socket.common.Const
import org.tio.core.intf.Packet
import java.io.UnsupportedEncodingException


/**
 */
class BlockPacket : Packet {
    /**
     * 消息类型，其值在Type中定义
     */
    /**
     * @return the type
     */
    /**
     * @param type
     * the type to set
     */
    var type: Byte = 0

    /**
     * @return the body
     */
    /**
     * @param body
     * the body to set
     */
    var body: ByteArray? = null

    constructor() : super() {}

    /**
     * @param type type
     * @param body body
     */
    constructor(type: Byte, body: ByteArray) : super() {
        this.type = type
        this.body = body
    }

    constructor(type: Byte, body: String) : super() {
        this.type = type
        setBody(body)
    }

    override fun logstr(): String {
        return "" + type
    }

    fun setBody(body: String) {
        try {
            this.body = body.toByteArray(Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    companion object {
        /**
         * 消息头的长度 1+4
         */
        val HEADER_LENGTH = 5
    }
}
