package cn.lannie.kt.blockchain.common

import cn.lannie.kt.blockchain.block.PairKey
import cn.lannie.kt.blockchain.common.algorithm.ECDSAAlgorithm
import cn.lannie.kt.blockchain.common.exception.ErrorNum
import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import org.apache.commons.codec.binary.Base64
import org.springframework.util.StringUtils
import java.io.UnsupportedEncodingException

object TrustSDK {

    /**
     * generatePairKey:生成私钥公钥对. <br></br>
     *
     * @param encodePubKey  是否压缩
     * @return PairKey
     * @throws TrustSDKException
     * TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    @JvmOverloads
    fun generatePairKey(encodePubKey: Boolean = false): PairKey {
        try {
            val pair = PairKey()
            val privateKey = ECDSAAlgorithm.generatePrivateKey()
            val pubKey = ECDSAAlgorithm.generatePublicKey(privateKey.trim({ it <= ' ' }), encodePubKey)
            pair.privateKey = (privateKey)
            pair.publicKey = (pubKey)
            return pair
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }

    /**
     * checkPairKey:验证一对公私钥是否匹配. <br></br>
     *
     * @param prvKey 输入 存放私钥 长度必须为PRVKEY_DIGEST_LENGTH
     * @param pubKey 输入 存放公钥 长度必须为PUBKEY_DIGEST_LENGTH
     * @return true 公私钥匹配  false 公私钥不匹配
     * @throws TrustSDKException TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    fun checkPairKey(prvKey: String, pubKey: String): Boolean {
        if (StringUtils.isEmpty(prvKey) || StringUtils.isEmpty(pubKey)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            val correctPubKey = ECDSAAlgorithm.generatePublicKey(prvKey.trim { it <= ' ' }, true)
            return pubKey.trim { it <= ' ' } == correctPubKey
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }

    /**
     * generatePubkeyByPrvkey: 通过私钥计算相应公钥. <br></br>
     *
     * @param privateKey
     * 私钥字符串
     * @param encode
     * 是否压缩公钥
     * @return 返回公钥字符串
     * @throws TrustSDKException
     * TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    @JvmOverloads
    fun generatePubkeyByPrvkey(privateKey: String, encode: Boolean = false): String {
        if (StringUtils.isEmpty(privateKey)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            return ECDSAAlgorithm.generatePublicKey(privateKey, encode)
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }

    @Throws(TrustSDKException::class)
    fun decodePubkey(encodePubKey: String): String {
        if (StringUtils.isEmpty(encodePubKey)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            return ECDSAAlgorithm.decodePublicKey(encodePubKey)
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }

    /**
     * generateAddrByPubkey:通过公钥获取对应地址. <br></br>
     *
     * @param pubKey
     * 公钥字符串
     * @return address
     * @throws TrustSDKException
     * TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    fun generateAddrByPubkey(pubKey: String): String {
        if (StringUtils.isEmpty(pubKey)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            return ECDSAAlgorithm.getAddress(Base64.decodeBase64(pubKey))
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }

    /**
     * generateAddrByPrvkey:通过私钥计算相应地址. <br></br>
     *
     * @param privateKey
     * 私钥字符串
     * @return Address
     * @throws TrustSDKException TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    fun generateAddrByPrvkey(privateKey: String): String {
        if (StringUtils.isEmpty(privateKey)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            val pubKey = ECDSAAlgorithm.generatePublicKey(privateKey)
            return generateAddrByPubkey(pubKey)
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }

    /**
     * signString:为字符串进行签名, 并返回签名. <br></br>
     *
     * @param privateKey
     * 私钥字符串
     * @param data
     * 需要签名的字符数组
     * @return 返回签名字符串
     * @throws TrustSDKException TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    fun signString(privateKey: String, data: ByteArray): String {
        if (StringUtils.isEmpty(privateKey)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            return ECDSAAlgorithm.sign(privateKey, data)
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.SIGN_ERROR.retCode, ErrorNum.SIGN_ERROR.retMsg, e)
        }

    }

    @Throws(TrustSDKException::class, UnsupportedEncodingException::class)
    fun signString(privateKey: String, data: String): String {
        return signString(privateKey, data.toByteArray(charset("UTF-8")))
    }

    /**
     * verifyString:验证一个签名是否有效. <br></br>
     *
     * @param pubKey
     * 公钥字符串
     * @param srcString
     * 源字符串
     * @param sign
     * 签名字符串
     * @return 返回验证是否通过 true:验证成功 false:验证失败
     * @throws TrustSDKException TrustSDKException
     * @since JDK 1.7
     */
    @Throws(TrustSDKException::class)
    fun verifyString(pubKey: String, srcString: String, sign: String): Boolean {
        if (StringUtils.isEmpty(pubKey) || StringUtils.isEmpty(srcString) || StringUtils.isEmpty(sign)) {
            throw TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.retCode, ErrorNum.INVALID_PARAM_ERROR.retMsg)
        }
        try {
            return ECDSAAlgorithm.verify(srcString, sign, pubKey)
        } catch (e: Exception) {
            throw TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.retCode, ErrorNum.ECDSA_ENCRYPT_ERROR.retMsg, e)
        }

    }


}
