package cn.lannie.kt.blockchain.core.service

import cn.lannie.kt.blockchain.block.PairKey
import cn.lannie.kt.blockchain.common.TrustSDK
import cn.lannie.kt.blockchain.common.exception.TrustSDKException
import org.springframework.stereotype.Service


/**
 */
@Service
class PairKeyService {

    /**
     * 生成公私钥对
     * @return PairKey
     * @throws TrustSDKException TrustSDKException
     */
    @Throws(TrustSDKException::class)
    fun generate(): PairKey {
        return TrustSDK.generatePairKey(true)
    }
}
