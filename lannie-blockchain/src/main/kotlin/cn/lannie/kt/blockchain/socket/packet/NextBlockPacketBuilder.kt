package cn.lannie.kt.blockchain.socket.packet

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.core.event.ClientRequestEvent
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.socket.body.RpcSimpleBlockBody

/**
 * 构建向别的节点请求next block的builder.带着自己最后一个block的hash
 */
object NextBlockPacketBuilder {
    fun build(): BlockPacket {
        return build(null)
    }

    fun build(responseId: String?): BlockPacket {
        val hash = ApplicationContextProvider.getBean(DbBlockManager::class.java).lastBlockHash

        val rpcBlockBody = RpcSimpleBlockBody(hash)
        rpcBlockBody.responseMsgId = responseId
        val blockPacket = PacketBuilder<RpcSimpleBlockBody>().setType(PacketType.NEXT_BLOCK_INFO_REQUEST).setBody(rpcBlockBody).build()
        //发布client请求事件
        ApplicationContextProvider.publishEvent(ClientRequestEvent(blockPacket))
        return blockPacket
    }

}
