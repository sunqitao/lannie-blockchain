package cn.lannie.kt.blockchain.common.algorithm

import cn.lannie.kt.blockchain.common.Constants
import org.apache.commons.codec.binary.Base64
import org.bouncycastle.asn1.x9.X9ECParameters
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.math.ec.FixedPointUtil
import java.lang.RuntimeException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

object ECDSAAlgorithm {
//    val CURVE:ECDomainParameters
    val CURVE_PARAMS:X9ECParameters = CustomNamedCurves.getByName("secp256K1")
    val baseAlgorithm = BaseAlgorithm
    val base58Algorithm = Base58AIgorithm
    var CURVE:ECDomainParameters
    var HALF_CURVE_ORDER:BigInteger
    init {
        FixedPointUtil.precompute(CURVE_PARAMS.g, 12)
        CURVE = ECDomainParameters(CURVE_PARAMS.curve,CURVE_PARAMS.g,CURVE_PARAMS.n,CURVE_PARAMS.h)
        HALF_CURVE_ORDER = CURVE_PARAMS.n.shiftRight(1)
    }

    fun generatePrivateKey():String{
        var secureRandom:SecureRandom
        try {
            secureRandom = SecureRandom.getInstance(Constants.RANDOM_NUMBER_ALGORITHM,Constants.RANDOM_NUMBER_ALGORITHM_PROVIDER)
        }catch (e:Exception){
            secureRandom = SecureRandom()
        }
        val privateKeyAttempt = ByteArray(32)
        secureRandom.nextBytes(privateKeyAttempt)
        var privateKeyCHeck = BigInteger(1,privateKeyAttempt)
        while (privateKeyCHeck.compareTo(BigInteger.ZERO)==0 || privateKeyCHeck.compareTo(Constants.MAXPRIVATEKEY) > 0 ){
            secureRandom.nextBytes(privateKeyAttempt)
            privateKeyCHeck = BigInteger(1,privateKeyAttempt)
        }
        var result = Base64.encodeBase64String(privateKeyAttempt)
        result = result.replace("[\\s*\t\n\r]","")
        return result
    }

    /**
     * 生成公钥 encode 为true时为短公钥
     * @param PRIVATEKEYBASE64String 私钥
     * @param encode 是否使用base64缩短
     * @return 公钥
     */
    fun generatePublicKey(privateKeyBase64String:String,encode:Boolean):String{
        try {
            val privateKeyBytes = Base64.decodeBase64(privateKeyBase64String)
            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val pointQ = spec.g.multiply(BigInteger(1,privateKeyBytes))
            var result = Base64.encodeBase64String(pointQ.getEncoded(encode))
            result = result.replace("[\\s*\t\n\r]","")
            return result
        }catch (e :Exception){
            throw RuntimeException(e)
        }
    }
    /**
     * 生成长公钥
     * @param privateKeyBase64String 私钥
     * @return 公钥
     */
    fun generatePublicKey(privateKeyBase64String : String):String{
        return generatePublicKey(privateKeyBase64String,false)
    }
    fun decodePublicKey(encodePubKeyBase64String:String):String{
        try {
            val encodePubkeyBytes = Base64.decodeBase64(encodePubKeyBase64String)
            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val pointQ = spec.g.curve.decodePoint(encodePubkeyBytes)
            var result = Base64.encodeBase64String(pointQ.getEncoded(false))
            result = result.replace("[\\s*\t\n\r]","")
            return result
        }catch (e : Exception){
            throw RuntimeException(e)
        }
    }
    /**
     * 根据公钥生成address
     * @param publicKey 公钥
     * @return ADDRESS
     */
    fun getAddress(publicKey:String):String{
        return getAddress(publicKey.toByteArray(Charsets.UTF_8),0)
    }
    /**
     * 根据公钥生成地址
     * @param keyBytes 公钥
     * @param version 版本，可以不用
     *
     */
    fun getAddress(keyBytes:ByteArray,vararg version:Int):String{
        val hashSha256 = baseAlgorithm.encode("SHA-256",keyBytes)
        val messageDigest = MessageDigest.getInstance("RipeMD160")
        messageDigest.update(hashSha256)
        val hashRipeMD160 = messageDigest.digest()
        val hashDoubleSha256 = baseAlgorithm.encodeTwice("SHA-256",hashRipeMD160)
        val rawAddr = ByteArray(1+hashRipeMD160.size+4)
        rawAddr[0] = 0
        System.arraycopy(hashRipeMD160,0,rawAddr,1,hashRipeMD160.size)
        System.arraycopy(hashDoubleSha256,0,rawAddr,hashRipeMD160.size+1,4)
        return base58Algorithm.encode(rawAddr)
    }
    fun sign(privateKey:String,data:String):String{
        return sign(privateKey,data.toByteArray(Charsets.UTF_8))
    }

    fun sign(privateKey:String,data:ByteArray):String{
        val hash256 = baseAlgorithm.encode("SHA-256",data)
        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest()))
        val pri = BigInteger(1,Base64.decodeBase64(privateKey))
        val privKey = ECPrivateKeyParameters(pri,CURVE)
        signer.init(true,privKey)
        val components = signer.generateSignature(hash256)
        val content = ECDSASignature(components[0],components[1],CURVE,HALF_CURVE_ORDER).toCanonicalised().encodeToDER()
        var result = Base64.encodeBase64String(content)
        result = result.replace("[\\s*\t\n\r]","")
        return result
    }

    /**
     * 根据公钥验证签名是否合法
     */
    fun verify(srcStr:String,sign:String,pubKey:String):Boolean{
        val hash256 = baseAlgorithm.encode("SHA-256",srcStr.toByteArray(Charsets.UTF_8))
        val eCDSASignature = ECDSASignature(CURVE,HALF_CURVE_ORDER).decodeFromDER(Base64.decodeBase64(sign))
        val signer = ECDSASigner()
        val  pub = CURVE.curve.decodePoint(Base64.decodeBase64(pubKey))
        val params = ECPublicKeyParameters(CURVE.curve.decodePoint(pub.getEncoded()),CURVE)
        signer.init(false,params)
        return signer.verifySignature(hash256,eCDSASignature.r,eCDSASignature.s)
    }
}