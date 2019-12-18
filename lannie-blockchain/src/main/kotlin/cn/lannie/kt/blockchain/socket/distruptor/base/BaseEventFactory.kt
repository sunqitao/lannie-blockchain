package cn.lannie.kt.blockchain.socket.distruptor.base

import com.lmax.disruptor.EventFactory


/**
 */
class BaseEventFactory : EventFactory<BaseEvent> {
    override fun newInstance(): BaseEvent {
        return BaseEvent()
    }

}
