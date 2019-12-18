package cn.lannie.kt.blockchain.common

/**
 * 权限
 */
interface PermissionType {
    companion object {
        /**
         * 表的创建者
         */
        val OWNER: Byte = 1
        /**
         * 所有权限
         */
        val ALL: Byte = 2
        val ADD: Byte = 3
        val UPDATE: Byte = 4
        val DELETE: Byte = 5
        /**
         * 不可见
         */
        val NONE: Byte = -1
    }
}
