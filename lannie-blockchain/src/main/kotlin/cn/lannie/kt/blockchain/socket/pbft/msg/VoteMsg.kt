package cn.lannie.kt.blockchain.socket.pbft.msg


/**
 * pbft算法传输prepare和commit消息的载体
 */
open class VoteMsg {
    /**
     * 当前投票状态（Prepare，commit）
     */
    var voteType: Byte = 0
    /**
     * 区块的hash
     */
    var hash: String? = null
    /**
     * 区块的number
     */
    var number: Int = 0
    /**
     * 是哪个节点传来的
     */
    var appId: String? = null
    /**
     * 是否同意
     */
    var isAgree: Boolean = false

    override fun toString(): String {
        return "VoteMsg{" +
                "voteType=" + voteType +
                ", hash='" + hash + '\''.toString() +
                ", number=" + number +
                ", appId='" + appId + '\''.toString() +
                ", agree=" + isAgree +
                '}'.toString()
    }
}
