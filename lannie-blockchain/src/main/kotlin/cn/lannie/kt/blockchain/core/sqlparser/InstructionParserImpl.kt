package cn.lannie.kt.blockchain.core.sqlparser

import cn.lannie.kt.blockchain.block.Instruction
import cn.lannie.kt.blockchain.block.InstructionBase
import cn.lannie.kt.blockchain.common.FastJsonUtil
import cn.lannie.kt.blockchain.core.model.base.BaseEntity
import cn.lannie.kt.blockchain.core.model.convert.ConvertTableName
import org.springframework.stereotype.Service
import javax.annotation.Resource


/**
 * 将区块内指令解析并入库
 */
@Service
class InstructionParserImpl<T : BaseEntity> : InstructionParser {
    @Resource
    private val convertTableName: ConvertTableName<T>? = null
    @Resource
    private val sqlParsers: Array<AbstractSqlParser<T>>? = null

    override fun parse(instructionBase: InstructionBase): Boolean {
        val operation = instructionBase.operation
        val table = instructionBase.table
        val json = instructionBase.oldJson
        //表对应的类名，如MessageEntity.class
        val clazz = convertTableName!!.convertOf(table!!)
        val `object` = FastJsonUtil().toBean(json!!, clazz)
        for (sqlParser in sqlParsers!!) {
            if (clazz == sqlParser.entityClass) {
                if (instructionBase is Instruction) {
                    `object`.publicKey = ((instructionBase as Instruction).publicKey)
                }
                sqlParser.parse(operation, instructionBase!!.instructionId!!, `object`)
                break
            }
        }

        return true
    }
}
