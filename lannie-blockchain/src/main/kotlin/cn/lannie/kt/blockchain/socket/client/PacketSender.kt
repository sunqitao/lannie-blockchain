package cn.lannie.kt.blockchain.socket.client

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.core.event.ClientRequestEvent
import cn.lannie.kt.blockchain.socket.common.Const.Companion.GROUP_NAME
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.springframework.stereotype.Component
import org.tio.client.ClientGroupContext
import org.tio.core.Aio
import javax.annotation.Resource

/**
 * 发送消息的工具类
 */
@Component
class PacketSender {
    @Resource
    private val clientGroupContext: ClientGroupContext? = null

    fun sendGroup(blockPacket: BlockPacket) {
        //对外发出client请求事件
        ApplicationContextProvider.publishEvent(ClientRequestEvent(blockPacket))
        //发送到一个group
        Aio.sendToGroup(clientGroupContext!!, GROUP_NAME, blockPacket)
    }

}
