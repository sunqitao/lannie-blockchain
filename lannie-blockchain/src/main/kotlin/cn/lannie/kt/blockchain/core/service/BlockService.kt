package cn.lannie.kt.blockchain.core.service

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.util.StrUtil
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.block.BlockHeader
import cn.lannie.kt.blockchain.block.Instruction
import cn.lannie.kt.blockchain.block.merkle.MerkleTree
import cn.lannie.kt.blockchain.common.CommonUtil
import cn.lannie.kt.blockchain.common.Sha256
import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.core.manager.PermissionManager
import cn.lannie.kt.blockchain.core.requestbody.BlockRequestBody
import cn.lannie.kt.blockchain.socket.body.RpcBlockBody
import cn.lannie.kt.blockchain.socket.client.PacketSender
import cn.lannie.kt.blockchain.socket.packet.PacketBuilder
import cn.lannie.kt.blockchain.socket.packet.PacketType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import javax.annotation.Resource


/**
 */
@Service
class BlockService {
    @Resource
    private val instructionService: InstructionService? = null
    @Value("\${version}")
    private val version: Int = 0
    @Resource
    private val packetSender: PacketSender? = null
    @Resource
    private val dbBlockManager: DbBlockManager? = null
    @Resource
    private val permissionManager: PermissionManager? = null

    /**
     * 校验指令集是否合法
     *
     * @param blockRequestBody
     * 指令集
     * @return 是否合法，为null则校验通过，其他则失败并返回原因
     */
    @Throws(TrustSDKException::class)
    fun check(blockRequestBody: BlockRequestBody?): String? {
        //TODO 此处可能需要校验publicKey的合法性
        if (blockRequestBody == null || blockRequestBody?.blockBody== null || StrUtil.isEmpty(blockRequestBody!!.publicKey)) {
            return "请求参数缺失"
        }
        val instructions = blockRequestBody.blockBody!!.instructions
        if (CollectionUtil.isEmpty(instructions)) {
            return "指令信息不能为空"
        }
        for (instruction in instructions!!) {
            if (!StrUtil.equals(blockRequestBody!!.publicKey, instruction.publicKey)) {
                return "指令内公钥和传来的公钥不匹配"
            }
            if (!instructionService!!.checkSign(instruction)) {
                return "签名校验不通过"
            }
            if (!instructionService!!.checkHash(instruction)) {
                return "Hash校验不通过"
            }
        }

        return if (!permissionManager!!.checkPermission(instructions)) {
            "权限校验不通过"
        } else null

    }

    /**
     * 添加新的区块
     * @param blockRequestBody blockRequestBody
     * @return Block
     */
    fun addBlock(blockRequestBody: BlockRequestBody): Block {
        val blockBody = blockRequestBody.blockBody
        val instructions = blockBody!!.instructions
        val hashList = instructions!!.stream().map{Instruction().hash}.collect(Collectors
                .toList<String>())

        val blockHeader = BlockHeader()
        blockHeader.hashList = hashList

        //计算所有指令的hashRoot
        blockHeader.hashMerkleRoot = (MerkleTree(hashList).build().root)
        blockHeader.publicKey = blockRequestBody.publicKey
        blockHeader.timeStamp = CommonUtil().now
        blockHeader.version = version
        blockHeader.number = dbBlockManager!!.lastBlockNumber + 1
        blockHeader.hashPreviousBlock = dbBlockManager!!.lastBlockHash
        val block = Block()
        block.blockBody = blockBody
        block.blockHeader = blockHeader
        block.hash = Sha256.sha256(blockHeader.toString() + blockBody.toString())

        val blockPacket = PacketBuilder<RpcBlockBody>().setType(PacketType.GENERATE_BLOCK_REQUEST).setBody(RpcBlockBody(block)).build()

        //广播给其他人做验证
        packetSender!!.sendGroup(blockPacket)

        return block
    }

}
