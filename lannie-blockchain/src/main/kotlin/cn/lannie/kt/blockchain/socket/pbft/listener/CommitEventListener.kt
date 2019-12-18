package cn.lannie.kt.blockchain.socket.pbft.listener

import cn.lannie.kt.blockchain.socket.body.VoteBody
import cn.lannie.kt.blockchain.socket.client.PacketSender
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import cn.lannie.kt.blockchain.socket.pbft.event.MsgCommitEvent
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 监听block可以commit消息
 */
@Component
class CommitEventListener {
    @Resource
    private val packetSender: PacketSender? = null

    /**
     * block已经开始进入commit状态，广播消息
     *
     * @param msgCommitEvent
     * msgCommitEvent
     */
    @EventListener
    fun msgIsCommit(msgCommitEvent: MsgCommitEvent) {
        val voteMsg = msgCommitEvent.getSource() as VoteMsg

        //群发消息，通知所有节点，我已对该Block Prepare
        val blockPacket = PacketBuilder<VoteBody>().setType(PacketType.PBFT_VOTE).setBody(VoteBody(voteMsg)).build()

        //广播给所有人我已commit
        packetSender!!.sendGroup(blockPacket)
    }
}