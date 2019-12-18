package cn.lannie.kt.blockchain.core.event

import org.springframework.context.ApplicationEvent
import org.tio.core.ChannelContext

/**
 * 节点连接完成时会触发该Event
 */
class NodesConnectedEvent(channelContext: ChannelContext) : ApplicationEvent(channelContext) {

    override fun getSource(): ChannelContext {
        return source as ChannelContext
    }

    companion object {
        private val serialVersionUID = 526755692642414178L
    }

}
