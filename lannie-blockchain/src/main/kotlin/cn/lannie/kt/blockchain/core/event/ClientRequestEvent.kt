package cn.lannie.kt.blockchain.core.event

import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import org.springframework.context.ApplicationEvent

/**
 * 客户端对外发请求时会触发该Event
 */
class ClientRequestEvent(blockPacket: BlockPacket) : ApplicationEvent(blockPacket)
