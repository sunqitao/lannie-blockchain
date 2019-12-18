package cn.lannie.kt.blockchain.common

import cn.hutool.crypto.digest.DigestUtil

object Sha256 {
    fun sha256(input: String): String {
        return DigestUtil.sha256Hex(input)
    }

}
