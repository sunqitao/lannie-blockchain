package cn.lannie.kt.blockchain.block.m

class MerkleProofHash(var hash: MerkleHash, var direction: Branch) {
    enum class Branch {
        LEFT,
        RIGHT,
        OLD_ROOT
    }

    override fun toString(): String {
        val hash = this.hash.toString()
        val direction = this.direction.toString()
        return "$hash  is $direction Child"
    }
}

