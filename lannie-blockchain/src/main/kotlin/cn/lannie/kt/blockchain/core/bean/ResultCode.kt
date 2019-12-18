package cn.lannie.kt.blockchain.core.bean

enum class ResultCode private constructor(var code: Int) {
    //成功
    SUCCESS(200),
    //失败
    FAIL(400),
    //未认证（签名错误）
    UNAUTHORIZED(401),
    //没有登录
    NO_LOGIN(402),
    //没有权限
    NO_PERMISSION(403),
    //接口不存在
    NOT_FOUND(404),
    //用户状态异常、公司状态异常、产品状态异常
    STATE_ERROR(406),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //参数错误
    PARAMETER_ERROR(10001),
    //账号错误
    ACCOUNT_ERROR(20001),
    //登录失败
    LOGIN_FAIL_ERROR(20002)
}