package cn.lannie.kt.blockchain.socket.server

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.base.AbstractAioHandler
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import cn.lannie.kt.blockchain.socket.distruptor.base.MessageProducer
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.tio.core.ChannelContext
import org.tio.core.intf.Packet
import org.tio.server.intf.ServerAioHandler


/**
 * server端处理所有client请求的入口
 */
class BlockServerAioHandler : AbstractAioHandler(), ServerAioHandler {


    /**
     * 自己是server，此处接收到客户端来的消息。这里是入口
     */
    override fun handler(packet: Packet, channelContext: ChannelContext) {
        val blockPacket = packet as BlockPacket

        //使用Disruptor来publish消息。所有收到的消息都进入Disruptor，同BlockClientAioHandler
        ApplicationContextProvider.getBean(MessageProducer::class.java).publish(BaseEvent(blockPacket, channelContext))
    }
}
