package cn.lannie.kt.blockchain.block


/**
 * 区块body，里面存放交易的数组
 */
class BlockBody {
    var instructions: List<Instruction>? = null

    override fun toString(): String {
        return "BlockBody{" +
                "instructions=" + instructions +
                '}'.toString()
    }
}
