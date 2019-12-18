package cn.lannie.kt.blockchain.socket.common

/**
 */
interface Const {
    companion object {
        /**
         * 服务器地址
         */
        val SERVER = "192.168.56.1"
        /**
         * 服务器分组名
         */
        val GROUP_NAME = "block_group"
        /**
         * 监听端口
         */
        val PORT = 6789

        /**
         * 心跳超时时间
         */
        val TIMEOUT = 5000

    }
}
