package cn.lannie.kt.blockchain.socket.body

import cn.lannie.kt.blockchain.block.Block

/**
 * body里是一个block信息
 */
open class RpcBlockBody : BaseBody {
    /**
     * blockJson
     */
    var block: Block? = null

    constructor() : super() {}

    constructor(block: Block?) : super() {
        this.block = block
    }

    override fun toString(): String {
        return "BlockBody{" +
                "block=" + block +
                '}'.toString()
    }
}
