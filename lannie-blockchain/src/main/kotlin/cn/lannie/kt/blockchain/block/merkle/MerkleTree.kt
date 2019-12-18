package cn.lannie.kt.blockchain.block.merkle

import cn.hutool.crypto.digest.DigestUtil
import java.util.ArrayList


/**
 * merkle tree简单实现
 *
 */
class MerkleTree
/**
 * constructor
 *
 * @param txList
 * transaction List 交易List
 */
(
        /**
         * transaction List
         */
        private val txList: List<String>) {
    /**
     * Merkle Root
     */
    /**
     * Get Root
     *
     * @return
     * 根节点hash
     */
    var root: String? = null
        private set

    init {
        root = ""
    }

    /**
     * execute merkle_tree and set root.
     */
    fun build(): MerkleTree {
        val tempTxList = ArrayList(this.txList)

        var newTxList = getNewTxList(tempTxList)

        while (newTxList.size != 1) {
            newTxList = getNewTxList(newTxList)
        }

        this.root = newTxList[0]
        return this
    }

    /**
     * return Node Hash List.
     *
     * @param tempTxList
     * list
     * @return
     * 某一层的左右节点相连hash
     */
    private fun getNewTxList(tempTxList: List<String>): List<String> {
        val newTxList = ArrayList<String>()
        var index = 0
        while (index < tempTxList.size) {
            // left
            val left = tempTxList[index]
            index++
            // right
            var right = ""
            if (index != tempTxList.size) {
                right = tempTxList[index]
            }
            // sha2 hex value
            val sha2HexValue = DigestUtil.sha256Hex(left + right)
            newTxList.add(sha2HexValue)
            index++
        }

        return newTxList
    }

}
