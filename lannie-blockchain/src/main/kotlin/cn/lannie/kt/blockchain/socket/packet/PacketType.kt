package cn.lannie.kt.blockchain.socket.packet

/**
 * packetType大于0时是请求类型，小于0时为响应类型
 */
interface PacketType {
    companion object {
        /**
         * 心跳包
         */
        val HEART_BEAT: Byte = 0
        /**
         * 已生成新的区块
         */
        val GENERATE_COMPLETE_REQUEST: Byte = 1
        /**
         * 已生成新的区块回应
         */
        val GENERATE_COMPLETE_RESPONSE: Byte = -1
        /**
         * 请求生成block
         */
        val GENERATE_BLOCK_REQUEST: Byte = 2
        /**
         * 同意、拒绝生成
         */
        val GENERATE_BLOCK_RESPONSE: Byte = -2
        /**
         * 获取所有block信息
         */
        val TOTAL_BLOCK_INFO_REQUEST: Byte = 3
        /**
         * 我的所有块信息
         */
        val TOTAL_BLOCK_INFO_RESPONSE: Byte = -3
        /**
         * 获取一个block信息
         */
        val FETCH_BLOCK_INFO_REQUEST: Byte = 4
        /**
         * 获取一块信息响应
         */
        val FETCH_BLOCK_INFO_RESPONSE: Byte = -4
        /**
         * 获取下一个区块的信息
         */
        val NEXT_BLOCK_INFO_REQUEST: Byte = 5
        /**
         * 获取下一个区块的信息
         */
        val NEXT_BLOCK_INFO_RESPONSE: Byte = -5
        /**
         * pbft投票
         */
        val PBFT_VOTE: Byte = 10
    }
}
