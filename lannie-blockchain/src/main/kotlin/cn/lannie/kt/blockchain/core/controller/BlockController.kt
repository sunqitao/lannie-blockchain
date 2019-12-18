package cn.lannie.kt.blockchain.core.controller

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.collection.CollectionUtil
import cn.lannie.kt.blockchain.block.BlockBody
import cn.lannie.kt.blockchain.block.Operation
import cn.lannie.kt.blockchain.block.check.BlockChecker
import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import cn.lannie.kt.blockchain.core.bean.BaseData
import cn.lannie.kt.blockchain.core.bean.ResultGenerator
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.core.manager.MessageManager
import cn.lannie.kt.blockchain.core.manager.SyncManager
import cn.lannie.kt.blockchain.core.requestbody.BlockRequestBody
import cn.lannie.kt.blockchain.core.requestbody.InstructionBody
import cn.lannie.kt.blockchain.core.service.BlockService
import cn.lannie.kt.blockchain.core.service.InstructionService
import cn.lannie.kt.blockchain.socket.client.PacketSender
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.annotation.Resource
import cn.hutool.core.collection.CollectionUtil.newArrayList
import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.core.event.DbSyncEvent
import cn.lannie.kt.blockchain.socket.body.RpcBlockBody
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault

@Api(tags = ["区块链接口"], description = "简单区块链功能接口")
@RestController
@RequestMapping("/block")
class BlockController {
    @Resource
    private val blockService: BlockService? = null
    @Resource
    private val packetSender: PacketSender? = null
    @Resource
    private val dbBlockManager: DbBlockManager? = null
    @Resource
    private val instructionService: InstructionService? = null
    @Resource
    private val syncManager: SyncManager? = null
    @Resource
    private val messageManager: MessageManager? = null
    @Resource
    private val blockChecker: BlockChecker? = null
    @Value("\${publicKey:A8WLqHTjcT/FQ2IWhIePNShUEcdCzu5dG+XrQU8OMu54}")
    private val publicKey: String? = null
    @Value("\${privateKey:yScdp6fNgUU+cRUTygvJG4EBhDKmOMRrK4XJ9mKVQJ8=}")
    private val privateKey: String? = null

    /**
     * 添加一个block，需要先在InstructionController构建1-N个instruction指令，然后调用该接口生成Block
     *
     * @param blockRequestBody
     * 指令的集合
     * @return 结果
     */
    @ApiIgnore
    @PostMapping("/insert")
    @ApiOperation(value = "添加一个区块", notes = "测试添加一个区块", httpMethod = "POST", response = BaseData::class)
    @Throws(TrustSDKException::class)
    fun insert(@ApiParam(name = "blockRequestBody对象", value = "传入json格式", required = true) @RequestBody blockRequestBody: BlockRequestBody): BaseData {
        val msg = blockService!!.check(blockRequestBody)
        return if (msg != null) {
            ResultGenerator.genFailResult(msg)
        } else ResultGenerator.genSuccessResult(blockService!!.addBlock(blockRequestBody))
    }

    /**
     * 测试生成一个insert:Block，公钥私钥可以通过PairKeyController来生成
     * @param content
     * sql内容
     */
    @GetMapping("/initfirst")
    @ApiOperation(value = "创建一个区块", notes = "创建一个新区块", httpMethod = "GET", response = BaseData::class)
    @Throws(Exception::class)
    fun create(@ApiParam(name = "content", value = "区块链内容", required = true) @RequestParam(value = "content") content: String): BaseData {
        val instructionBody = InstructionBody()
        instructionBody.operation = Operation.ADD
        instructionBody.table = "message"
        instructionBody.json = "{\"content\":\"$content\"}"
        /*instructionBody.setPublicKey("A8WLqHTjcT/FQ2IWhIePNShUEcdCzu5dG+XrQU8OMu54");
        instructionBody.setPrivateKey("yScdp6fNgUU+cRUTygvJG4EBhDKmOMRrK4XJ9mKVQJ8=");*/
        instructionBody.publicKey = publicKey
        instructionBody.privateKey = privateKey
        val instruction = instructionService!!.build(instructionBody)

        val blockRequestBody = BlockRequestBody()
        blockRequestBody.publicKey = instructionBody.publicKey
        val blockBody = BlockBody()

        blockBody.instructions = CollUtil.newArrayList(instruction) //newArrayList(instruction)

        blockRequestBody.blockBody = blockBody

        return ResultGenerator.genSuccessResult(blockService!!.addBlock(blockRequestBody))
    }

    /**
     * 测试生成一个update:Block，公钥私钥可以通过PairKeyController来生成
     * @param id 更新的主键
     * @param content
     * sql内容
     */
    @GetMapping("/update")
    @ApiOperation(value = "更新区块链内容", notes = "根据ID更新区块链内容", httpMethod = "GET", response = BaseData::class)
    @Throws(Exception::class)
    fun testUpdate(@ApiParam(name = "id", value = "区块链信息编号", required = true) @RequestParam(value = "id", required = true) id: String,
                   @ApiParam(name = "content", value = "区块链内容", required = true) @RequestParam(value = "content") content: String): BaseData {
        if (StringUtils.isBlank(id)) ResultGenerator.genSuccessResult("主键不可为空")
        val instructionBody = InstructionBody()
        instructionBody.operation = Operation.UPDATE
        instructionBody.table = "message"
        instructionBody.instructionId = id
        instructionBody.json = "{\"content\":\"$content\"}"
        /*instructionBody.setPublicKey("A8WLqHTjcT/FQ2IWhIePNShUEcdCzu5dG+XrQU8OMu54");
        instructionBody.setPrivateKey("yScdp6fNgUU+cRUTygvJG4EBhDKmOMRrK4XJ9mKVQJ8=");*/
        instructionBody.publicKey = publicKey
        instructionBody.privateKey = privateKey
        val instruction = instructionService!!.build(instructionBody)

        val blockRequestBody = BlockRequestBody()
        blockRequestBody.publicKey = instructionBody.publicKey
        val blockBody =  BlockBody()
        blockBody.instructions = CollectionUtil.newArrayList(instruction)

        blockRequestBody.blockBody = blockBody

        return ResultGenerator.genSuccessResult(blockService!!.addBlock(blockRequestBody))
    }

    /**
     * 测试生成一个delete:Block，公钥私钥可以通过PairKeyController来生成
     * @param id 待删除记录的主键
     * sql内容
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除区块内容", notes = "删除区块链内容", httpMethod = "GET", response = BaseData::class)
    @Throws(Exception::class)
    fun delete(@ApiParam(name = "id", value = "区块链信息编号", required = true) @RequestParam(value = "id", required = true) id: String): BaseData {
        if (StringUtils.isBlank(id)) ResultGenerator.genSuccessResult("主键不可为空")
        val instructionBody = InstructionBody()
        instructionBody.operation = Operation.DELETE
        instructionBody.table = "message"
        instructionBody.instructionId = id
        val message = messageManager!!.findById(id)
        val content = if (ObjectUtils.isEmpty(message)) "" else message.content
        instructionBody.json = "{\"content\":\"$content\"}"
        /*instructionBody.setPublicKey("A8WLqHTjcT/FQ2IWhIePNShUEcdCzu5dG+XrQU8OMu54");
        instructionBody.setPrivateKey("yScdp6fNgUU+cRUTygvJG4EBhDKmOMRrK4XJ9mKVQJ8=");*/
        instructionBody.publicKey = publicKey
        instructionBody.privateKey = privateKey
        val instruction = instructionService!!.build(instructionBody)

        val blockRequestBody = BlockRequestBody()
        blockRequestBody.publicKey = instructionBody.publicKey
        val blockBody = BlockBody()
        blockBody.instructions = CollectionUtil.newArrayList(instruction)

        blockRequestBody.blockBody = blockBody

        return ResultGenerator.genSuccessResult(blockService!!.addBlock(blockRequestBody))
    }

    /**
     * 查询已落地的sqlite里的所有数据
     */
    @ApiOperation(value = "查询区块链数据", notes = "查询区块链数据", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/sqlite")
    fun sqlite(): BaseData {
        return ResultGenerator.genSuccessResult(messageManager?.findAll())
    }

    /**
     * 查询已落地的sqlite里content字段
     */
    @ApiOperation(value = "查询区块链内容", notes = "查询区块链内容", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/sqlite/content")
    fun content(): BaseData {
        return ResultGenerator.genSuccessResult(messageManager!!.findAllContent())
    }

    /**
     * 获取最后一个block的信息
     */
    @ApiOperation(value = "获取最后一个块信息", notes = "获取最后一个块信息", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/last")
    fun last(): BaseData {
        return ResultGenerator.genSuccessResult(dbBlockManager!!.lastBlock)
    }

    /**
     * 手工执行区块内sql落地到sqlite操作
     * @param pageable
     * 分页
     * @return
     * 已同步到哪块了的信息
     */
    @ApiIgnore
    @ApiOperation(value = "手工执行区块内sql落地到sqlite操作", notes = "获取数据同步到的区块信息", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/sync")
    fun sync(@PageableDefault pageable: Pageable): BaseData {
        ApplicationContextProvider.publishEvent(DbSyncEvent(""))
        return ResultGenerator.genSuccessResult(syncManager!!.findAll(pageable))
    }

    /**
     * 全量检测区块是否正常
     * @return
     * null - 通过
     * hash - 第一个异常hash
     */
    @ApiIgnore
    @ApiOperation(value = "全量检测区块是否正常", notes = "全量检测区块是否正常", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/check")
    fun check(): BaseData {

        var block = dbBlockManager!!.firstBlock

        var hash: String? = null
        while (block != null && hash == null) {
            hash = blockChecker!!.checkBlock(block)
            block = dbBlockManager!!.getNextBlock(block)
        }
        return ResultGenerator.genSuccessResult(hash)
    }

    /**
     * 获取第一个区块信息
     */
    @ApiOperation(value = "获取第一个区块信息", notes = "获取第一个区块信息", httpMethod = "GET", response = BaseData::class)
    @GetMapping("/first")
    fun first(): BaseData {
        val block = dbBlockManager!!.firstBlock
        val packet = PacketBuilder<RpcBlockBody>()
                .setType(PacketType.NEXT_BLOCK_INFO_REQUEST)
                .setBody(RpcBlockBody(block)).build()
        packetSender!!.sendGroup(packet)
        return ResultGenerator.genSuccessResult(block)
    }


}
