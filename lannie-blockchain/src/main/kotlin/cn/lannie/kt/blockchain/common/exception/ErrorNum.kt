package cn.lannie.kt.blockchain.common.exception

enum class ErrorNum {

    INVALID_PARAM_ERROR("001", "参数错误"),
    DES3_ENCRYPT_ERROR("002", "DES3加解密错误"),
    AES_ENCRYPT_ERROR("003", "AES加解密错误"),
    ECDSA_ENCRYPT_ERROR("004", "ECDSA加解密错误"),
    SIGN_ERROR("005", "签名错误"),
    GENERATE_SIGN_ERROR("006", "生成签名错误"),
    GENERATE_SQL_ERROR("007", "生成SQL错误"),
    VERIFY_SIGN_ERROR("008", "验证签名错误");

    var retCode:String
    var retMsg:String
    constructor(retCode:String,retMsg:String){
        this.retCode = retCode
        this.retMsg = retMsg
    }
}