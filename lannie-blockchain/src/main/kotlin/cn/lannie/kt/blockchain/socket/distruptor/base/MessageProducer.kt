package cn.lannie.kt.blockchain.socket.distruptor.base

/**
 */
interface MessageProducer {
    fun publish(baseEvent: BaseEvent)
}
