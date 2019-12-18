package cn.lannie.kt.blockchain.core.controller

import cn.lannie.kt.blockchain.core.bean.BaseData
import cn.lannie.kt.blockchain.core.bean.ResultGenerator
import cn.lannie.kt.blockchain.core.requestbody.InstructionBody
import cn.lannie.kt.blockchain.core.service.InstructionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore
import javax.annotation.Resource

/**
 * 区块body内单个指令的controller
 */
@ApiIgnore
@RestController
@RequestMapping("/instruction")
class InstructionController {
    @Resource
    private val instructionService: InstructionService? = null

    /**
     * 构建一条指令，传入各必要参数
     * @param instructionBody instructionBody
     * @return
     * 用私钥签名后的指令
     */
    @PostMapping
    @Throws(Exception::class)
    fun build(@RequestBody instructionBody: InstructionBody): BaseData {
        if (!instructionService!!.checkKeyPair(instructionBody)) {
            return ResultGenerator.genFailResult("公私钥不是一对")
        }
        return if (!instructionService!!.checkContent(instructionBody)) {
            ResultGenerator.genFailResult("Delete和Update操作需要有id和json内容")
        } else ResultGenerator.genSuccessResult(instructionService!!.build(instructionBody))
    }
}
