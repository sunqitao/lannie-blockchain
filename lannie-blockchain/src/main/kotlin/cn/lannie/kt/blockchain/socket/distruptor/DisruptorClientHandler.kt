package cn.lannie.kt.blockchain.socket.distruptor

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import com.lmax.disruptor.EventHandler


/**
 */
class DisruptorClientHandler : EventHandler<BaseEvent> {

    @Throws(Exception::class)
    override fun onEvent(baseEvent: BaseEvent, sequence: Long, endOfBatch: Boolean) {
        ApplicationContextProvider.getBean(DisruptorClientConsumer::class.java).receive(baseEvent)
    }
}
