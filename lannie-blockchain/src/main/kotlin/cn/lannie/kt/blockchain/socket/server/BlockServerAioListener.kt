package cn.lannie.kt.blockchain.socket.server

import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext
import org.tio.core.intf.Packet
import org.tio.server.intf.ServerAioListener
import org.tio.utils.json.Json


/**
 * 连接状态的监听器
 * 2017年3月26日 下午8:22:31
 */
class BlockServerAioListener : ServerAioListener {

    override fun onAfterConnected(channelContext: ChannelContext, isConnected: Boolean, isReconnect: Boolean) {
        log.info("onAfterConnected channelContext:{}, isConnected:{}, isReconnect:{}", channelContext, isConnected, isReconnect)

        //连接后，需要把连接会话对象设置给channelContext
        //channelContext.setAttribute(new ShowcaseSessionContext());
    }

    @Throws(Exception::class)
    override fun onAfterDecoded(channelContext: ChannelContext, packet: Packet, i: Int) {

    }

    @Throws(Exception::class)
    override fun onAfterReceivedBytes(channelContext: ChannelContext, i: Int) {
        log.info("onAfterReceived channelContext:{}, packet:{}, packetSize:{}")
    }


    override fun onAfterSent(channelContext: ChannelContext, packet: Packet, isSentSuccess: Boolean) {
        log.info("onAfterSent channelContext:{}, packet:{}, isSentSuccess:{}", channelContext, Json.toJson(packet), isSentSuccess)
    }

    @Throws(Exception::class)
    override fun onAfterHandled(channelContext: ChannelContext, packet: Packet, l: Long) {

    }

    override fun onBeforeClose(channelContext: ChannelContext, throwable: Throwable, remark: String, isRemove: Boolean) {}

    companion object {
        private val log = LoggerFactory.getLogger(BlockServerAioListener::class.java)
    }
}
