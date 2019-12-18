package cn.lannie.kt.blockchain.block.check

import cn.lannie.kt.blockchain.block.Block


/**
 * 区块校验
 */
interface BlockChecker {
    /**
     * 比较目标区块和自己本地的区块num大小
     * @param block
     * 被比较的区块
     * @return
     * 本地与目标区块的差值
     */
    fun checkNum(block: Block): Int

    /**
     * 校验区块内操作的权限是否合法
     * @param block
     * block
     * @return
     * 大于0合法
     */
    fun checkPermission(block: Block): Int

    /**
     * 校验hash，包括prevHash、内部hash（merkle tree root hash）
     * @param block
     * block
     * @return
     * 大于0合法
     */
    fun checkHash(block: Block): Int

    /**
     * 校验生成时间
     * @param block  block
     * @return block
     */
    fun checkTime(block: Block): Int

    /**
     * 校验签名
     * @param block  block
     * @return block
     */
    fun checkSign(block: Block): Int

    /**
     * 校验block，包括签名、hash、关联关系
     * @param block
     * @return
     */
    fun checkBlock(block: Block): String?

}
