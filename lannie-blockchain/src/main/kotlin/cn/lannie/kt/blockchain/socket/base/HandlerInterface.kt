package cn.lannie.kt.blockchain.socket.base

import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.tio.core.ChannelContext


/**
 * 业务处理器接口
 */
interface HandlerInterface {

    /**
     * handler方法在此封装转换
     * @param packet packet
     * @param channelContext channelContext
     * @return Object对象
     * @throws Exception  Exception
     */
    @Throws(Exception::class)
    fun handler(packet: BlockPacket, channelContext: ChannelContext): Any?

}
