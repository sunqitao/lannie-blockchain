package cn.lannie.kt.blockchain.core.manager

import cn.hutool.core.util.StrUtil
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.block.db.DbStore
import cn.lannie.kt.blockchain.common.Constants
import cn.lannie.kt.blockchain.common.FastJsonUtil
import org.springframework.stereotype.Service
import javax.annotation.Resource


/**
 */
@Service
class DbBlockManager {
    @Resource
    private val dbStore: DbStore? = null

    /**
     * 查找第一个区块
     *
     * @return 第一个Block
     */
    val firstBlock: Block?
        get() {
            val firstBlockHash = dbStore!!.get(Constants.KEY_FIRST_BLOCK)
            return if (StrUtil.isEmpty(firstBlockHash)) {
                null
            } else getBlockByHash(firstBlockHash!!)
        }

    /**
     * 获取最后一个区块信息
     *
     * @return 最后一个区块
     */
    val lastBlock: Block?
        get() {
            val lastBlockHash = dbStore!!.get(Constants.KEY_LAST_BLOCK)
            return if (StrUtil.isEmpty(lastBlockHash)) {
                null
            } else getBlockByHash(lastBlockHash!!)
        }

    /**
     * 获取最后一个区块的hash
     *
     * @return hash
     */
    val lastBlockHash: String?
        get() {
            val block = lastBlock
            return if (block != null) {
                block!!.hash
            } else null
        }

    /**
     * 获取最后一个block的number
     * @return number
     */
    val lastBlockNumber: Int
        get() {
            val block = lastBlock
            return if (block != null) {
                block!!.blockHeader!!.number
            } else 0
        }

    /**
     * 获取某一个block的下一个Block
     *
     * @param block
     * block
     * @return block
     */
    fun getNextBlock(block: Block?): Block? {
        if (block == null) {
            return firstBlock
        }
        val nextHash = dbStore!!.get(Constants.KEY_BLOCK_NEXT_PREFIX + block!!.hash) ?: return null
        return getBlockByHash(nextHash)
    }

    fun getNextBlockByHash(hash: String?): Block? {
        if (hash == null) {
            return firstBlock
        }
        val nextHash = dbStore!!.get(Constants.KEY_BLOCK_NEXT_PREFIX + hash) ?: return null
        return getBlockByHash(nextHash)
    }

    fun getBlockByHash(hash: String): Block {
        val blockJson = dbStore!!.get(hash)
        return FastJsonUtil().toBean(blockJson!!, Block::class.java)
    }

}
