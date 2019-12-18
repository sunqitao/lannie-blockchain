package cn.lannie.kt.blockchain.socket.server

import cn.lannie.kt.blockchain.socket.common.Const
import org.springframework.stereotype.Component
import org.tio.server.AioServer
import org.tio.server.ServerGroupContext
import java.io.IOException
import javax.annotation.PostConstruct


/**
 * server启动器
 *
 */
@Component
class BlockServerStarter {

    @PostConstruct
    @Throws(IOException::class)
    fun serverStart() {
        val serverAioHandler = BlockServerAioHandler()
        val serverAioListener = BlockServerAioListener()
        val serverGroupContext = ServerGroupContext(serverAioHandler, serverAioListener)
        val aioServer = AioServer(serverGroupContext)
        //本机启动服务
        aioServer.start(null, Const.PORT)
    }
}
