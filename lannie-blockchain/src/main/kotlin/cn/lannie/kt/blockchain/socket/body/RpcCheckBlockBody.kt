package cn.lannie.kt.blockchain.socket.body

import cn.lannie.kt.blockchain.block.Block


/**
 * 校验block是否合法，同意、拒绝区块生成请求
 */
class RpcCheckBlockBody : RpcBlockBody {
    /**
     * 0是正常同意，-1区块number错误，-2没有权限，-3hash错误，-4时间错误，-10不合法的next block
     */
    var code: Int = 0
    /**
     * 附带的message
     */
    var message: String? = null

    constructor() {}

    constructor(code: Int, message: String, block: Block) : super(block) {
        this.code = code
        this.message = message
    }


    constructor(code: Int, message: String) : super(null) {
        this.code = code
        this.message = message
    }

    override fun toString(): String {
        return "RpcCheckBlockBody{" +
                "code=" + code +
                ", message='" + message + '\''.toString() +
                '}'.toString()
    }
}
