package cn.lannie.kt.blockchain.socket.body

/**
 * 请求next block时用的包装类
 */
class RpcNextBlockBody : BaseBody {
    /**
     * blockHash
     */
    var hash: String? = null
    /**
     * 上一个hash
     */
    var prevHash: String? = null

    constructor() : super() {}

    constructor(hash: String?, prevHash: String?) : super() {
        this.hash = hash
        this.prevHash = prevHash
    }

    override fun toString(): String {
        return "RpcNextBlockBody{" +
                "hash='" + hash + '\''.toString() +
                ", prevHash='" + prevHash + '\''.toString() +
                '}'.toString()
    }
}
