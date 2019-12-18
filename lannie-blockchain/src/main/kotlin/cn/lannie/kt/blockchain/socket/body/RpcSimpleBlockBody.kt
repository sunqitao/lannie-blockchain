package cn.lannie.kt.blockchain.socket.body

/**
 */
class RpcSimpleBlockBody : BaseBody {
    /**
     * blockHash
     */
    var hash: String? = null

    constructor() : super() {}

    constructor(hash: String?) : super() {
        this.hash = hash
    }

    override fun toString(): String {
        return "RpcSimpleBlockBody{" +
                "hash='" + hash + '\''.toString() +
                '}'.toString()
    }
}
