package cn.lannie.kt.blockchain.socket.base

import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.tio.core.ChannelContext
import org.tio.core.GroupContext
import org.tio.core.exception.AioDecodeException
import org.tio.core.intf.AioHandler
import org.tio.core.intf.Packet
import java.nio.ByteBuffer


/**
 * 2017年3月27日 上午12:14:12
 */
abstract class AbstractAioHandler : AioHandler {
    /**
     * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
     * 消息头：type + bodyLength
     * 消息体：byte[]
     */
    @Throws(AioDecodeException::class)
    override fun decode(buffer: ByteBuffer, channelContext: ChannelContext): BlockPacket? {
        val readableLength = buffer.limit() - buffer.position()
        if (readableLength < BlockPacket.HEADER_LENGTH) {
            return null
        }

        //消息类型
        val type = buffer.get()

        val bodyLength = buffer.int

        if (bodyLength < 0) {
            throw AioDecodeException("bodyLength [$bodyLength] is not right, remote:" + channelContext
                    .clientNode)
        }

        val neededLength = BlockPacket.HEADER_LENGTH + bodyLength
        val test = readableLength - neededLength
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (test < 0) {
            return null
        }
        val imPacket = BlockPacket()
        imPacket.type = type
        if (bodyLength > 0) {
            val dst = ByteArray(bodyLength)
            buffer.get(dst)
            imPacket.body = dst
        }
        return imPacket
    }

    /**
     * 编码：把业务消息包编码为可以发送的ByteBuffer
     * 消息头：type + bodyLength
     * 消息体：byte[]
     */
    override fun encode(packet: Packet, groupContext: GroupContext, channelContext: ChannelContext): ByteBuffer {
        val showcasePacket = packet as BlockPacket
        val body = showcasePacket.body
        var bodyLen = 0
        if (body != null) {
            bodyLen = body!!.size
        }

        //总长度是消息头的长度+消息体的长度
        val allLen = BlockPacket.HEADER_LENGTH + bodyLen

        val buffer = ByteBuffer.allocate(allLen)
        buffer.order(groupContext.byteOrder)

        //写入消息类型
        buffer.put(showcasePacket.type)
        //写入消息体长度
        buffer.putInt(bodyLen)

        //写入消息体
        if (body != null) {
            buffer.put(body!!)
        }
        return buffer
    }
}
