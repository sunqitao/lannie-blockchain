package cn.lannie.kt.blockchain.socket.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.tio.client.ClientGroupContext
import org.tio.client.ReconnConf


/**
 * 配置ClientGroupContext
 */
@Configuration
class ClientContextConfig {

    /**
     * 构建客户端连接的context
     * @return
     * ClientGroupContext
     */
    @Bean
    fun clientGroupContext(): ClientGroupContext {
        //handler, 包括编码、解码、消息处理
        val clientAioHandler = BlockClientAioHandler()
        //事件监听器，可以为null，但建议自己实现该接口
        val clientAioListener = BlockClientAioListener()
        //断链后自动连接的，不想自动连接请设为null
        val reconnConf = ReconnConf(5000L, 20)

        //clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        return ClientGroupContext(clientAioHandler, clientAioListener,
                reconnConf)
    }

}
