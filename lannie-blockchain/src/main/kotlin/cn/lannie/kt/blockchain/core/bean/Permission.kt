package cn.lannie.kt.blockchain.core.bean


/**
 * 权限，主要存储各member对表的权限信息，如不可见、只能ADD，可以UPDATE、DELETE等等组合
 */
class Permission {
    /**
     * 哪张表
     */
    var tableName: String? = null
    /**
     * 操作权限，见PermissionType类
     */
    var permissionType: Byte = 0
    /**
     * 公钥（账户的概念，能具体到某个member，为*则代表所有节点，不具体指定某个）
     */
    var publicKey: String? = null
    /**
     * 该权限是归属于哪个group的。节点只需要获取自己group的权限信息，不需要知道别的group的
     */
    var groupId: String? = null

    override fun toString(): String {
        return "Permission{" +
                "tableName='" + tableName + '\''.toString() +
                ", permissionType=" + permissionType +
                ", publicKey='" + publicKey + '\''.toString() +
                ", groupId='" + groupId + '\''.toString() +
                '}'.toString()
    }
}
