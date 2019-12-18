package cn.lannie.kt.blockchain.block


/**
 * 区块头
 */
class BlockHeader {
    /**
     * 版本号
     */
    var version: Int = 0
    /**
     * 上一区块的hash
     */
    var hashPreviousBlock: String? = null
    /**
     * merkle tree根节点hash
     */
    var hashMerkleRoot: String? = null
    /**
     * 生成该区块的公钥
     */
    var publicKey: String? = null
    /**
     * 区块的序号
     */
    var number: Int = 0
    /**
     * 时间戳
     */
    var timeStamp: Long = 0
    /**
     * 32位随机数
     */
    var nonce: Long = 0
    /**
     * 该区块里每条交易信息的hash集合，按顺序来的，通过该hash集合能算出根节点hash
     */
    var hashList: List<String>? = null

    override fun toString(): String {
        return "BlockHeader{" +
                "version=" + version +
                ", hashPreviousBlock='" + hashPreviousBlock + '\''.toString() +
                ", hashMerkleRoot='" + hashMerkleRoot + '\''.toString() +
                ", publicKey='" + publicKey + '\''.toString() +
                ", number=" + number +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                ", hashList=" + hashList +
                '}'.toString()
    }
}
