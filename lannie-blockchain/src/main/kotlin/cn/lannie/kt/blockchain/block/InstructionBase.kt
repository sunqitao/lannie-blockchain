package cn.lannie.kt.blockchain.block

/**
* blockBody内一条指令的基础属性
*/
open class InstructionBase {
    /**
     * 指令的操作，增删改（1，-1，2）
     */
     var operation: Byte = 0
    /**
     * 操作的表名
     */
     var table: String? = null
    /**
     * 最终要执行入库的json内容
     */
     var oldJson: String? = null
    /**
     * 业务id，sql语句中where需要该Id
     */
     var instructionId: String? = null




    override fun toString(): String {
        return "InstructionReverse{" +
                "operation=" + operation +
                ", table='" + table + '\''.toString() +
                ", oldJson='" + oldJson + '\''.toString() +
                ", instructionId='" + instructionId + '\''.toString() +
                '}'.toString()
    }
}
