package cn.lannie.kt.blockchain.block

/**
 */
interface Operation {
    companion object {
        val ADD: Byte = 1
        val DELETE: Byte = -1
        val UPDATE: Byte = 2
    }

}
