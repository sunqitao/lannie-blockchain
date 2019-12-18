package cn.lannie.kt.blockchain.socket.distruptor

import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import cn.lannie.kt.blockchain.socket.distruptor.base.MessageConsumer
import cn.lannie.kt.blockchain.socket.handler.server.*
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.springframework.stereotype.Component
import java.util.HashMap

/**
 * 所有client发来的消息都在这里处理
 */
@Component
class DisruptorServerConsumer : MessageConsumer {
    companion object {
        private val handlerMap = HashMap<Byte, AbstractBlockHandler<*>>()
        init {
            handlerMap.put(PacketType.GENERATE_COMPLETE_REQUEST, GenerateCompleteRequestHandler())
            handlerMap.put(PacketType.GENERATE_BLOCK_REQUEST, GenerateBlockRequestHandler())
            handlerMap.put(PacketType.TOTAL_BLOCK_INFO_REQUEST, TotalBlockInfoRequestHandler())
            handlerMap.put(PacketType.FETCH_BLOCK_INFO_REQUEST, FetchBlockRequestHandler())
            handlerMap.put(PacketType.HEART_BEAT, HeartBeatHandler())
            handlerMap.put(PacketType.NEXT_BLOCK_INFO_REQUEST, NextBlockRequestHandler())
            handlerMap.put(PacketType.PBFT_VOTE, PbftVoteHandler())
        }
    }

    @Throws(Exception::class)
    override fun receive(baseEvent: BaseEvent) {
        val blockPacket = baseEvent.blockPacket
        val type = blockPacket!!.type
        val handler = handlerMap.get(type) ?: return
        handler.handler(blockPacket, baseEvent.channelContext!!)
    }


}

