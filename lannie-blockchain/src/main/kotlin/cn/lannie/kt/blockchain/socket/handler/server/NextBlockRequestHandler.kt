package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.RpcNextBlockBody
import cn.lannie.kt.blockchain.socket.body.RpcSimpleBlockBody
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.slf4j.LoggerFactory
import org.tio.core.Aio
import org.tio.core.ChannelContext
import org.tio.utils.json.Json


/**
 * 获取某个区块下一块的请求，发起方带着自己的lastBlock hash，接收方则将自己的区块中，在传来的hash后面的那块返回回去。
 *
 *
 * 如A传来了3，而我本地有5个区块，则返回区块4。
 */
class NextBlockRequestHandler : AbstractBlockHandler<RpcSimpleBlockBody>() {
    private val logger = LoggerFactory.getLogger(TotalBlockInfoRequestHandler::class.java!!)

    override fun bodyClass(): Class<RpcSimpleBlockBody> {
        return RpcSimpleBlockBody::class.java
    }

    override fun handler(packet: BlockPacket, rpcBlockBody: RpcSimpleBlockBody, channelContext: ChannelContext): Any? {
        logger.info("收到来自于<" + rpcBlockBody.appId + ">的<请求下一Block>消息，请求者的block hash为：" + Json.toJson(rpcBlockBody.hash))
        //传来的Block，如果为null，说明发起方连一个Block都没有
        val hash = rpcBlockBody.hash

        //查询自己的next block hash，返回给对方，让对方搜集2f+1后确定哪个是对的
        val nextBlock = ApplicationContextProvider.getBean(DbBlockManager::class.java).getNextBlockByHash(hash)
        var nextHash: String? = null
        if (nextBlock != null) {
            nextHash = nextBlock!!.hash
        }
        val respBody = RpcNextBlockBody(nextHash, hash)
        respBody.responseMsgId = (rpcBlockBody.messageId)
        val blockPacket = PacketBuilder<RpcNextBlockBody>().setType(PacketType
                .NEXT_BLOCK_INFO_RESPONSE).setBody(respBody).build()
        Aio.send(channelContext, blockPacket)
        logger.info("回复给<" + rpcBlockBody.appId + ">，我的nextBlock是" + respBody.toString())

        return null
    }
}
