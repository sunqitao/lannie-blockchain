package cn.lannie.kt.blockchain.common.algorithm

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESAlgorithm {
    /**
     * aesEncode: aes 加密
     * key：秘钥
     * data： 明文
     */
    fun aesEncode(key:ByteArray,data:ByteArray):ByteArray{
        var cipher = Cipher.getInstance("/AES/ECB/PKCS5Padding")
        var secretKey = SecretKeySpec(key,"AES")
        cipher.init(Cipher.ENCRYPT_MODE,secretKey)
        return cipher.doFinal(data)
    }

    /**
     * aesDecode: aes解密
     */
    fun aesDecode(key:ByteArray,encryptedText:ByteArray):ByteArray{
        var cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
        var secretKey = SecretKeySpec(key,"AES")
        cipher.init(Cipher.DECRYPT_MODE,secretKey)
        return cipher.doFinal(encryptedText)
    }

}
//
//fun main() {
//    var aesAlgorithm = AESAlgorithm()
//    println(aesAlgorithm.aesEncode("123".toByteArray(),"sun".toByteArray()))
//}