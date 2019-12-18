package cn.lannie.kt.blockchain.block.check.local

import cn.hutool.core.util.StrUtil
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.block.check.BlockChecker
import cn.lannie.kt.blockchain.common.Sha256
import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.core.manager.PermissionManager
import cn.lannie.kt.blockchain.core.requestbody.BlockRequestBody
import cn.lannie.kt.blockchain.core.service.BlockService
import org.springframework.stereotype.Component
import javax.annotation.Resource


/**
 * 使用本地存储的权限、Block信息对新来的block进行校验
 */
@Component
class DbBlockChecker : BlockChecker {
    @Resource
    private val dbBlockManager: DbBlockManager? = null
    @Resource
    private val permissionManager: PermissionManager? = null

    @Resource
    private val blockService: BlockService? = null

    private val lastBlock: Block?
        get() = dbBlockManager!!.lastBlock

    override fun checkNum(block: Block): Int {
        val localBlock = lastBlock
        var localNum = 0
        if (localBlock != null) {
            localNum = localBlock!!.blockHeader!!.number
        }
        //本地区块+1等于新来的区块时才同意
        return if (localNum + 1 == block.blockHeader!!.number) {
            //同意生成区块
            0
        } else -1

        //拒绝
    }

    override fun checkPermission(block: Block): Int {
        //校验对表的操作权限
        return if (permissionManager!!.checkPermission(block)) 0 else -1
    }

    override fun checkHash(block: Block): Int {
        val localLast = lastBlock
        //创世块可以，或者新块的prev等于本地的last hash也可以
        if (localLast == null && block.blockHeader!!.hashPreviousBlock == null) {
            return 0
        }
        return if (localLast != null && StrUtil.equals(localLast!!.hash, block.blockHeader!!.hashPreviousBlock)) {
            0
        } else -1
    }

    override fun checkTime(block: Block): Int {
        val localBlock = lastBlock
        //新区块的生成时间比本地的还小
        return if (localBlock != null && block.blockHeader!!.timeStamp <= localBlock!!.blockHeader!!.timeStamp) {
            //拒绝
            -1
        } else 0
    }

    override fun checkSign(block: Block): Int {
        return if (!checkBlockHashSign(block)) {
            -1
        } else 0
    }

    override fun checkBlock(block: Block): String? {
        if (!checkBlockHashSign(block)) return block.hash

        val preHash = block.blockHeader!!.hashPreviousBlock ?: return null

        val preBlock = dbBlockManager!!.getBlockByHash(preHash) ?: return block.hash

        val localNum = preBlock.blockHeader!!.number
        //当前区块+1等于下一个区块时才同意
        if (localNum + 1 != block.blockHeader!!.number) {
            return block.hash
        }
        return if (block!!.blockHeader!!.timeStamp <= preBlock!!.blockHeader!!.timeStamp) {
            block.hash
        } else null


    }

    /**
     * 检测区块签名及hash是否符合
     * @param block
     * @return
     */
    private fun checkBlockHashSign(block: Block): Boolean {
        val blockRequestBody = BlockRequestBody(block.blockHeader?.publicKey,block.blockBody)

        try {
            if (blockService?.check(blockRequestBody) != null) return false
        } catch (e: TrustSDKException) {
            return false
        }

        val hash = Sha256.sha256(block.blockHeader.toString() + block.blockBody.toString())
        return if (!StrUtil.equals(block.hash, hash)) false else true

    }

}
