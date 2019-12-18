package cn.lannie.kt.blockchain.block


/**
 * 区块body内一条指令
 */
class Instruction : InstructionBase() {
    /**
     * 新的内容
     */
    var json: String? = null
    /**
     * 时间戳
     */
    var timeStamp: Long? = null
    /**
     * 操作人的公钥
     */
    var publicKey: String? = null
    /**
     * 签名
     */
    var sign: String? = null
    /**
     * 该操作的hash
     */
    var hash: String? = null

    override fun toString(): String {
        return "Instruction{" +
                "json='" + json + '\''.toString() +
                ", timeStamp=" + timeStamp +
                ", publicKey='" + publicKey + '\''.toString() +
                ", sign='" + sign + '\''.toString() +
                ", hash='" + hash + '\''.toString() +
                '}'.toString()
    }
}
