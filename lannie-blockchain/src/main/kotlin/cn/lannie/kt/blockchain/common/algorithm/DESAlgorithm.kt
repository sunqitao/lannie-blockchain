package cn.lannie.kt.blockchain.common.algorithm

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec

object DESAlgorithm{
    /**
     * 密钥算法
     */
    val KEY_ALGORITHM:String = "DESede"
    /**
     * 加密/解密算法/工作模式/填充方式
     */
    val CIPHER_ALGORITHM :String = "DESede/ECB/PKCS5Padding"

    /**
     * 转换密钥
     */
    fun toKey(key:ByteArray):Key{
        //实例化des密钥
        var dks = DESedeKeySpec(key)
        //实例化密钥工厂
        var keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM)
        //生成密钥
        return keyFactory.generateSecret(dks)
    }

    /**
     * 加密数据
     * @param data 待加密数据
     * @param key 密钥
     */
    fun encrypt(data:ByteArray,key:ByteArray):ByteArray{
        //还原密钥
        var k = toKey(key)
        //实例化
        var cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        //初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE,k)
        //执行操作
        return cipher.doFinal(data)
    }

    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     */
    fun decrypt(data:ByteArray,key:ByteArray):ByteArray{
        //还原密钥
        var k = toKey(key)
        //实例化
        var cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE,k)
        return cipher.doFinal(data)
    }
}