package cn.lannie.kt.blockchain.socket.client

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.core.event.NodesConnectedEvent
import org.slf4j.LoggerFactory
import org.tio.client.intf.ClientAioListener
import org.tio.core.Aio
import org.tio.core.ChannelContext
import org.tio.core.intf.Packet


/**
 * client端对各个server连接的情况回调。
 * 当某个server的心跳超时（2min）时，Aio会从group里remove掉该连接，需要在重新connect后重新加入group
 *
 */
class BlockClientAioListener : ClientAioListener {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(Exception::class)
    override fun onAfterConnected(channelContext: ChannelContext, isConnected: Boolean, isReconnect: Boolean) {
        //        if (isConnected) {
        //            logger.info("连接成功：server地址为-" + channelContext.getServerNode());
        //            Aio.bindGroup(channelContext, Const.GROUP_NAME);
        //        } else {
        //            logger.info("连接失败：server地址为-" + channelContext.getServerNode());
        //        }
        ApplicationContextProvider.publishEvent(NodesConnectedEvent(channelContext))
    }

    override fun onBeforeClose(channelContext: ChannelContext, throwable: Throwable, s: String, b: Boolean) {
        logger.info("连接关闭：server地址为-" + channelContext.serverNode)
        Aio.unbindGroup(channelContext)
    }

    @Throws(Exception::class)
    override fun onAfterDecoded(channelContext: ChannelContext, packet: Packet, i: Int) {

    }

    @Throws(Exception::class)
    override fun onAfterReceivedBytes(channelContext: ChannelContext, i: Int) {

    }

    @Throws(Exception::class)
    override fun onAfterSent(channelContext: ChannelContext, packet: Packet, b: Boolean) {

    }

    @Throws(Exception::class)
    override fun onAfterHandled(channelContext: ChannelContext, packet: Packet, l: Long) {

    }

}
