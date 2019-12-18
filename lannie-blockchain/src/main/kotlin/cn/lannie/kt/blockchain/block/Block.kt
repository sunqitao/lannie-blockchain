package cn.lannie.kt.blockchain.block

import cn.hutool.crypto.digest.DigestUtil


/**
 * 区块
 */
class Block {
    /**
     * 区块头
     */
    var blockHeader: BlockHeader? = null
    /**
     * 区块body
     */
    var blockBody: BlockBody? = null
    /**
     * 该区块的hash
     */
    var hash: String? = null

    /**
     * 根据该区块所有属性计算sha256
     * @return
     * sha256hex
     */
    private fun calculateHash(): String {
        return DigestUtil.sha256Hex(
                blockHeader!!.toString() + blockBody!!.toString()
        )
    }

    override fun toString(): String {
        return "Block{" +
                "blockHeader=" + blockHeader +
                ", blockBody=" + blockBody +
                ", hash='" + hash + '\''.toString() +
                '}'.toString()
    }
}
