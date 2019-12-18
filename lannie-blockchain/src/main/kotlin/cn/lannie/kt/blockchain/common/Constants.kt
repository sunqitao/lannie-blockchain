package cn.lannie.kt.blockchain.common

import java.math.BigInteger

class Constants {
    companion object{
        val PUBKEY_DIGEST_LENGTH = 90 //public key length
        val PRVKEY_DIGEST_LENGTH = 45 //private key length
        val ADDR_DIGEST_LENGTH = 35 //address length
        val SIGN_DIGEST_LENGTH = 98 //signature length
        val KEY_DES3_DIGEST_LENGTH = 24 //max size of key for des3 encrypt
        val KEY_AES128_DIGEST_LENGTH = 16 //max size of key for aes128 encrypt
        val RANSSQL_DIGEST_LENGTH = 8192 // max size of trans sql for trustsql

        val RANDOM_NUMBER_ALGORITHM = "SHA1PRNG"
        val RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN"
        val MAXPRIVATEKEY = BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140",16)
        val INFO_SHARE_PUBKEY = "BC8s/4qEAvVl4Sv0LwQOWJcVU6Q5hBd+7LlJeEivVmUbdtwP4RTfN8x/G+muMhN8SrweyyVVMIcIrnMWoFqGfIA="

        /**
         * 最后一个区块 hash的key，value就是最后一个区块的hash
         */
        val KEY_LAST_BLOCK = "key_last_block"
        /**
         * 第一个区块hash的key，value就是第一个区块的hash
         */
        val KEY_FIRST_BLOCK = "key_first_block"
        /**
         * 区块hash与区块本身的key value映射，key的前缀，如{key_block_xxxxxxx -> blockJson}
         */
        val KEY_BLCOK_HASH_PREFIX = "key_block_"
        val KEY_REQUEST_PREFIX= "key_request_"
        /**
         * 保存区块的hash和下一区块hash，key为hash，value为下一块hash
         */
        val KEY_BLOCK_NEXT_PREFIX = "key_next_"
        /**
         * 每个表的权限存储key
         */
        val KEY_PERMISSION = "key_permission_"
    }
}