package cn.lannie.kt.blockchain.core.bean

open class BaseData {
    var code: Int = 0
     var message: String? = null
     var data: Any? = null

    override fun toString(): String {
        return "BaseData{" +
                "code=" + code +
                ", message='" + message + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }

    fun setCode(resultCode: ResultCode): BaseData {
        this.code = resultCode.code
        return this
    }



    fun setCode(code: Int): BaseData {
        this.code = code
        return this
    }



    fun setMessage(message: String): BaseData {
        this.message = message
        return this
    }



    fun setData(data: Any?): BaseData {
        this.data = data
        return this
    }
}
