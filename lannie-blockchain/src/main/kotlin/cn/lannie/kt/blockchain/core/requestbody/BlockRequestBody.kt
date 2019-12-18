package cn.lannie.kt.blockchain.core.requestbody

import cn.lannie.kt.blockchain.block.BlockBody

/**
 * 生成Block时传参
 */
class BlockRequestBody {
    var publicKey: String? = null
    var blockBody: BlockBody? = null

    constructor(publicKey: String?,blockBody: BlockBody?){
        this.publicKey = publicKey
        this.blockBody = blockBody
    }

    constructor()

    override fun toString(): String {
        return "BlockRequestBody{" +
                "publicKey='" + publicKey + '\''.toString() +
                ", blockBody=" + blockBody +
                '}'.toString()
    }
}
