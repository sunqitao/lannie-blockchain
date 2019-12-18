package cn.lannie.kt.blockchain.block.check

import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.socket.body.RpcCheckBlockBody
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 区块校验
 */
@Component
class CheckerManager {
    @Resource
    private val blockChecker: BlockChecker? = null

    /**
     * 基本校验
     * @param block block
     * @return 校验结果
     */
    fun check(block: Block): RpcCheckBlockBody {
        val code = blockChecker!!.checkSign(block)
        if (code != 0) {
            return RpcCheckBlockBody(-1, "block的签名不合法")
        }

        val number = blockChecker.checkNum(block)
        if (number != 0) {
            return RpcCheckBlockBody(-1, "block的number不合法")
        }
        val time = blockChecker.checkTime(block)
        if (time != 0) {
            return RpcCheckBlockBody(-4, "block的时间错误")
        }
        val hash = blockChecker.checkHash(block)
        if (hash != 0) {
            return RpcCheckBlockBody(-3, "hash校验不通过")
        }
        val permission = blockChecker.checkPermission(block)
        return if (permission != 0) {
            RpcCheckBlockBody(-2, "没有表的操作权限")
        } else RpcCheckBlockBody(0, "OK", block)

    }

}
