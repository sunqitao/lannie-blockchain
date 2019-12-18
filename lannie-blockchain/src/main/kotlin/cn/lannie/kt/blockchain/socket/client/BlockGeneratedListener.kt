package cn.lannie.kt.blockchain.socket.client

import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.core.event.AddBlockEvent
import cn.lannie.kt.blockchain.socket.body.RpcSimpleBlockBody
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 本地新生成区块后，需要通知所有group内的节点
 */
@Component
class BlockGeneratedListener {
    @Resource
    private val packetSender: PacketSender? = null

    @Order(2)
    @EventListener(AddBlockEvent::class)
    fun blockGenerated(addBlockEvent: AddBlockEvent) {
        val block = addBlockEvent.getSource() as Block
        val blockPacket = PacketBuilder<RpcSimpleBlockBody>().setType(PacketType.GENERATE_COMPLETE_REQUEST).setBody(RpcSimpleBlockBody(block.hash!!)).build()

        //广播给其他人做验证
        packetSender!!.sendGroup(blockPacket)
    }
}
