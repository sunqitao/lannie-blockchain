package cn.lannie.kt.blockchain.socket.base

import cn.lannie.kt.blockchain.socket.body.BaseBody
import cn.lannie.kt.blockchain.socket.common.Const
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.tio.core.ChannelContext
import org.tio.utils.json.Json


/**
 * 基础handler
 */
abstract class AbstractBlockHandler<T : BaseBody> : HandlerInterface {

    abstract fun bodyClass(): Class<T>

    @Throws(Exception::class)
    override fun handler(packet: BlockPacket, channelContext: ChannelContext): Any? {
        val jsonStr: String
        var bsBody: T? = null
        if (packet.body != null) {
            jsonStr = String(packet.body!!, Charsets.UTF_8)
            bsBody = Json.toBean(jsonStr, bodyClass())
        }

        return handler(packet, bsBody!!, channelContext)
    }

    /**
     * 实际的handler处理
     * @param packet packet
     * @param bsBody 解析后的对象
     * @param channelContext channelContext
     * @return 用不上
     * @throws Exception Exception
     */
    @Throws(Exception::class)
    abstract fun handler(packet: BlockPacket, bsBody: T, channelContext: ChannelContext): Any?

}
