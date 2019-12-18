package cn.lannie.kt.blockchain.socket.distruptor

import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import cn.lannie.kt.blockchain.socket.distruptor.base.MessageProducer
import com.lmax.disruptor.dsl.Disruptor

/**
 * 所有客户端、server端发来的消息，都进入这里，然后publish出去，供消费者消费
 */
class DisruptorProducer(private val disruptor: Disruptor<BaseEvent>) : MessageProducer {

    override fun publish(baseEvent: BaseEvent) {
        val ringBuffer = disruptor.getRingBuffer()
        val sequence = ringBuffer.next()
        try {
            // Get the entry in the Disruptor
            val event = ringBuffer.get(sequence)
            // for the sequence   // Fill with data
            event.blockPacket = baseEvent.blockPacket
            event.channelContext = baseEvent.channelContext
        } finally {
            ringBuffer.publish(sequence)
        }
    }
}
