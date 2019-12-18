package cn.lannie.kt.blockchain.socket.pbft.listener

import cn.lannie.kt.blockchain.socket.body.VoteBody
import cn.lannie.kt.blockchain.socket.client.PacketSender
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import cn.lannie.kt.blockchain.socket.pbft.event.MsgPrepareEvent
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 */
@Component
class PrepareEventListener {
    @Resource
    private val packetSender: PacketSender? = null

    /**
     * block已经开始进入Prepare状态
     *
     * @param msgPrepareEvent
     * msgIsPrepareEvent
     */
    @EventListener
    fun msgIsPrepare(msgPrepareEvent: MsgPrepareEvent) {
        val voteMsg = msgPrepareEvent.getSource() as VoteMsg

        //群发消息，通知别的节点，我已对该Block Prepare
        val blockPacket = PacketBuilder<VoteBody>().setType(PacketType.PBFT_VOTE).setBody(VoteBody(voteMsg)).build()

        //广播给所有人我已Prepare
        packetSender!!.sendGroup(blockPacket)
    }
}
