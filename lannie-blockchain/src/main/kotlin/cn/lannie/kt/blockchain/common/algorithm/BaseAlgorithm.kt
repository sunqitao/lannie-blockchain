package cn.lannie.kt.blockchain.common.algorithm

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.lang.Exception
import java.lang.RuntimeException
import java.security.MessageDigest
import java.security.Security

object BaseAlgorithm {
    init {
        Security.addProvider(BouncyCastleProvider())
    }

    fun encode(algorithm:String,data : ByteArray):ByteArray{
        if(data == null){
            return ByteArray(0)
        }
        try {
            var messageDigest = MessageDigest.getInstance(algorithm)
            messageDigest.update(data)
            return messageDigest.digest()
        }catch (e : Exception){
            throw RuntimeException(e)
        }
    }

    fun encodeTwice(algorithm:String,data:ByteArray):ByteArray{
        if(data == null){
            return ByteArray(0)
        }
        try {
            var messageDigest = MessageDigest.getInstance(algorithm)
            messageDigest.update(data)
            return messageDigest.digest(messageDigest.digest())
        }catch (e : Exception){
            throw RuntimeException(e)
        }
    }

}