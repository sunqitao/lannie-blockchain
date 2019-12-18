package cn.lannie.kt.blockchain.socket.distruptor

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import com.lmax.disruptor.EventHandler
import org.slf4j.LoggerFactory


/**
 */
class DisruptorServerHandler : EventHandler<BaseEvent> {

    private val logger = LoggerFactory.getLogger(DisruptorServerHandler::class.java)

    @Throws(Exception::class)
    override fun onEvent(baseEvent: BaseEvent, sequence: Long, endOfBatch: Boolean) {
        try {
            ApplicationContextProvider.getBean(DisruptorServerConsumer::class.java).receive(baseEvent)
        } catch (e: Exception) {
            logger.error("Disruptor事件执行异常", e)
        }

    }
}
