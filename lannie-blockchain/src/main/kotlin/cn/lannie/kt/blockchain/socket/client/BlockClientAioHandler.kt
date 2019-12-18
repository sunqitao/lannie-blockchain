package cn.lannie.kt.blockchain.socket.client

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.base.AbstractAioHandler
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import cn.lannie.kt.blockchain.socket.distruptor.base.MessageProducer
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.tio.client.intf.ClientAioHandler
import org.tio.core.ChannelContext
import org.tio.core.intf.Packet

/**
 */
class BlockClientAioHandler : AbstractAioHandler(), ClientAioHandler {

    override fun heartbeatPacket(): BlockPacket? {
        //心跳包的内容就是隔一段时间向别的节点获取一次下一步区块（带着自己的最新Block获取别人的next Block）
        //return NextBlockPacketBuilder.build();
        return null
    }

    /**
     * server端返回的响应会先进到该方法，将消息全丢到Disruptor中
     */
    override fun handler(packet: Packet, channelContext: ChannelContext) {
        val blockPacket = packet as BlockPacket

        //使用Disruptor来publish消息。所有收到的消息都进入Disruptor，同BlockServerAioHandler
        ApplicationContextProvider.getBean(MessageProducer::class.java).publish(BaseEvent(blockPacket, channelContext))
    }
}
