package cn.lannie.kt.blockchain.socket.distruptor

import cn.hutool.core.util.StrUtil
import cn.lannie.kt.blockchain.common.AppId
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.BaseBody
import cn.lannie.kt.blockchain.socket.distruptor.base.BaseEvent
import cn.lannie.kt.blockchain.socket.distruptor.base.MessageConsumer
import cn.lannie.kt.blockchain.socket.handler.client.FetchBlockResponseHandler
import cn.lannie.kt.blockchain.socket.handler.client.NextBlockResponseHandler
import cn.lannie.kt.blockchain.socket.handler.client.TotalBlockInfoResponseHandler
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.tio.utils.json.Json
import java.util.HashMap


/**
 * 所有server发来的消息都在这里处理
 */
@Component
class DisruptorClientConsumer : MessageConsumer {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(Exception::class)
    override fun receive(baseEvent: BaseEvent) {
        val blockPacket = baseEvent.blockPacket
        val type = blockPacket!!.type
        val blockHandler = handlerMap.get(type) ?: return

        //消费消息
        val baseBody = Json.toBean(String(blockPacket!!.body!!), BaseBody::class.java)
        //logger.info("收到来自于<" + baseBody.getAppId() + ">针对msg<" + baseBody.getResponseMsgId() + ">的回应");

        val appId = baseBody.appId
        if (StrUtil.equals(AppId.value, appId)) {
            //是本机
            //return;
        }

        blockHandler.handler(blockPacket, baseEvent.channelContext!!)
    }

    companion object {
        private val handlerMap = HashMap<Byte, AbstractBlockHandler<*>>()

        init {
            handlerMap.put(PacketType.TOTAL_BLOCK_INFO_RESPONSE, TotalBlockInfoResponseHandler())
            handlerMap.put(PacketType.NEXT_BLOCK_INFO_RESPONSE, NextBlockResponseHandler())
            handlerMap.put(PacketType.FETCH_BLOCK_INFO_RESPONSE, FetchBlockResponseHandler())
        }
    }
}
