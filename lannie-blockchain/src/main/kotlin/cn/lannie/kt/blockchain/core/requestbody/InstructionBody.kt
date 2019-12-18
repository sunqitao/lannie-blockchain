package cn.lannie.kt.blockchain.core.requestbody


/**
 */
class InstructionBody {
    /**
     * 指令的操作，增删改
     */
    var operation: Byte = 0
    /**
     * 操作的表名
     */
    var table: String? = null
    /**
     * 具体内容
     */
    var json: String? = null
    /**
     * 原始内容
     */
    var oldJson: String? = null
    /**
     * 业务id
     */
    var instructionId: String? = null
    /**
     * 私钥
     */
    var privateKey: String? = null
    /**
     * 公钥
     */
    var publicKey: String? = null

    override fun toString(): String {
        return "InstructionBody{" +
                "operation=" + operation +
                ", table='" + table + '\''.toString() +
                ", json='" + json + '\''.toString() +
                ", oldJson='" + oldJson + '\''.toString() +
                ", instructionId='" + instructionId + '\''.toString() +
                ", privateKey='" + privateKey + '\''.toString() +
                ", publicKey='" + publicKey + '\''.toString() +
                '}'.toString()
    }
}
