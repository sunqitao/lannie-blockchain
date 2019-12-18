package cn.lannie.kt.blockchain.core.service

import cn.hutool.core.bean.BeanUtil
import cn.lannie.kt.blockchain.block.Instruction
import cn.lannie.kt.blockchain.block.InstructionReverse
import cn.lannie.kt.blockchain.block.Operation
import cn.lannie.kt.blockchain.common.CommonUtil
import cn.lannie.kt.blockchain.common.Sha256
import cn.lannie.kt.blockchain.common.TrustSDK
import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import cn.lannie.kt.blockchain.core.requestbody.InstructionBody
import org.springframework.stereotype.Service


/**
 * 一条指令的service
 *
 */
@Service
class InstructionService {
    /**
     * 校验公私钥是不是一对
     *
     * @param instructionBody
     * instructionBody
     * @return boolean
     * @throws TrustSDKException
     * TrustSDKException
     */
    @Throws(TrustSDKException::class)
    fun checkKeyPair(instructionBody: InstructionBody): Boolean {
        return TrustSDK.checkPairKey(instructionBody.privateKey!!, instructionBody.publicKey!!)
    }

    /**
     * 校验内容的合法性
     * @param instructionBody instructionBody
     * @return true false
     */
    fun checkContent(instructionBody: InstructionBody): Boolean {
        val operation = instructionBody.operation
        return if (operation != Operation.ADD && operation != Operation.DELETE && operation != Operation.UPDATE) {
            false
        } else Operation.UPDATE !== operation && Operation.DELETE !== operation || instructionBody.instructionId != null && instructionBody.json != null && instructionBody.oldJson != null
        //不是add时，必须要有id和json和原始json
    }

    /**
     * 根据传来的body构建一条指令
     *
     * @param instructionBody
     * body
     * @return Instruction
     */
    @Throws(Exception::class)
    fun build(instructionBody: InstructionBody): Instruction {
        val instruction = Instruction()
        BeanUtil.copyProperties(instructionBody, instruction)
        if (Operation.ADD === instruction.operation) {
            instruction.instructionId = CommonUtil().generateUuid()
        }
        instruction.timeStamp = CommonUtil().now
        val buildStr = getSignString(instruction)
        //设置签名，供其他人验证
        instruction.sign = (TrustSDK.signString(instructionBody.privateKey!!, buildStr))
        //设置hash，防止篡改
        instruction.hash = Sha256.sha256(buildStr)

        return instruction
    }

    private fun getSignString(instruction: Instruction): String {
        return instruction.operation.toString() + instruction.table + instruction
                .instructionId + if (instruction.json == null) "" else instruction.json
    }

    /**
     * 根据一个指令，计算它的回滚时的指令。
     *
     *
     * 如add table1 {id:xxx, name:"123"}，那么回滚时就是delete table1 {id:xxx}
     * 如delete table2 id2 oldJson:{id:xxx, name:"123"}，那么回滚时就是add table2 {id:xxx, name:"123"}。
     * 如update table3 id3 json:{id:xxx, name:"123"} oldJson:{id:xxx, name:"456"}
     * 注意，更新和删除时，原来的json都得有，不然没法回滚
     *
     * @param instruction
     * instruction
     * @return 回滚指令
     */
    fun buildReverse(instruction: Instruction): InstructionReverse {
        val instructionReverse = InstructionReverse()
        BeanUtil.copyProperties(instruction, instructionReverse)

        if (Operation.ADD === instruction.operation) {
            instructionReverse.operation = (Operation.DELETE)
        } else if (Operation.DELETE === instruction.operation) {
            instructionReverse.operation = (Operation.ADD)
        }

        return instructionReverse
    }

    @Throws(TrustSDKException::class)
    fun checkSign(instruction: Instruction): Boolean {
        val buildStr = getSignString(instruction)
        return TrustSDK.verifyString(instruction.publicKey!!, buildStr, instruction.sign!!)
    }

    fun checkHash(instruction: Instruction): Boolean {
        val buildStr = getSignString(instruction)
        return Sha256.sha256(buildStr).equals(instruction.hash)
    }
}
