package cn.lannie.kt.blockchainmanager.constant

class PermissionType{
    companion object{
        const val OWNER:Byte = 1
        const val ALL:Byte = 2
        const val ADD:Byte = 3
        const val UPDATE:Byte = 4
        const val DELETE:Byte = 5
        const val NONE:Byte = -1

    }
}