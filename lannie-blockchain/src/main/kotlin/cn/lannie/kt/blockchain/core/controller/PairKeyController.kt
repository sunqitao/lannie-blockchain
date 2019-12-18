package cn.lannie.kt.blockchain.core.controller

import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import cn.lannie.kt.blockchain.core.bean.BaseData
import cn.lannie.kt.blockchain.core.bean.ResultGenerator
import cn.lannie.kt.blockchain.core.service.PairKeyService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@Api(tags = ["区块链接口"], description = "公私钥接口")
@RestController
@RequestMapping("/pairKey")
class PairKeyController {
    @Resource
    private val pairKeyService: PairKeyService? = null

    /**
     * 生成公钥私钥
     */
    @ApiOperation(value = "区块链公私钥接口", notes = "生成区块链节点公私钥", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/random")
    @Throws(TrustSDKException::class)
    fun generate(): BaseData {
        return ResultGenerator.genSuccessResult(pairKeyService!!.generate())
    }
}
