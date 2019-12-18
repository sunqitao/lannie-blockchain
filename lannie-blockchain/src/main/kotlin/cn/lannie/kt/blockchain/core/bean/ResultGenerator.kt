package cn.lannie.kt.blockchain.core.bean

object ResultGenerator {
    private val DEFAULT_SUCCESS_MESSAGE = "SUCCESS"

    fun genSuccessResult(): BaseData {
        return BaseData()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
    }

    fun genSuccessResult(data: Any?): BaseData {
        return BaseData()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data)
    }

    fun genFailResult(message: String): BaseData {
        return BaseData()
                .setCode(ResultCode.FAIL)
                .setMessage(message)
    }

    fun genFailResult(resultCode: ResultCode, message: String): BaseData {
        return BaseData()
                .setCode(resultCode)
                .setMessage(message)
    }
}
