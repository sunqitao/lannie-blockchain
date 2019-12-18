package cn.lannie.kt.blockchain.socket.distruptor.base

/**
 */
interface MessageConsumer {
    @Throws(Exception::class)
    fun receive(baseEvent: BaseEvent)
}
