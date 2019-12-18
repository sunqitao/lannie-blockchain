package cn.lannie.kt.blockchain.socket.distruptor

import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEventFactory
import cn.lannie.kt.blockchain.socket.distruptor.base.MessageProducer
import com.lmax.disruptor.BlockingWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

/**
 */
@Configuration
class DisruptorConfig {

    private fun disruptor(): Disruptor<BaseEvent> {
        val producerFactory = Executors.defaultThreadFactory()
        val eventFactory = BaseEventFactory()
        val bufferSize = 1024
        val disruptor = Disruptor<BaseEvent>(eventFactory, bufferSize, producerFactory,
                ProducerType.SINGLE, BlockingWaitStrategy())
        //两个消费者，任何消息都会同时被两个消费者消费，消费者会根据type来判断哪个是该自己处理的
        disruptor.handleEventsWith(DisruptorServerHandler(), DisruptorClientHandler())

        disruptor.start()

        return disruptor
    }

    @Bean
    fun messageProducer(): MessageProducer {
        return DisruptorProducer(disruptor())
    }
}
