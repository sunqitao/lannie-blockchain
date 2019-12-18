package cn.lannie.kt.blockchain.socket.handler.server

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.common.timer.TimerManager
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.socket.base.AbstractBlockHandler
import cn.lannie.kt.blockchain.socket.body.RpcSimpleBlockBody
import cn.lannie.kt.blockchain.socket.client.PacketSender
import cn.lannie.kt.blockchain.socket.packet.BlockPacket
import cn.lannie.kt.blockchain.socket.packet.NextBlockPacketBuilder
import org.slf4j.LoggerFactory
import org.tio.core.ChannelContext
import java.util.function.Supplier

/**
 * 已生成了新区块的全网广播
 */
class GenerateCompleteRequestHandler : AbstractBlockHandler<RpcSimpleBlockBody>() {
    private val logger = LoggerFactory.getLogger(GenerateCompleteRequestHandler::class.java)

    override fun bodyClass(): Class<RpcSimpleBlockBody> {
        return RpcSimpleBlockBody::class.java
    }

    override fun handler(packet: BlockPacket, rpcBlockBody: RpcSimpleBlockBody, channelContext: ChannelContext): Any? {
        logger.info("收到来自于<" + rpcBlockBody.appId + "><生成了新的Block>消息，block hash为[" + rpcBlockBody.hash+
                "]")

        //延迟2秒校验一下本地是否有该区块，如果没有，则发请求去获取新Block
        //延迟的目的是可能刚好自己也马上就要生成同样的Block了，就可以省一次请求
        TimerManager().schedule(Supplier{
            val block = ApplicationContextProvider.getBean(DbBlockManager::class.java).getBlockByHash(rpcBlockBody
                    .hash!!)
            //本地有了
            if (block == null) {
                logger.info("开始去获取别人的新区块")
                //在这里发请求，去获取group别人的新区块
                val nextBlockPacket = NextBlockPacketBuilder.build()
                ApplicationContextProvider.getBean(PacketSender::class.java).sendGroup(nextBlockPacket)
            }
            null
        }, 2000)

        return null
    }
}
