package cn.lannie.kt.blockchainmanager.model

import java.util.*


/**
 * 权限，主要存储各个member对表的权限信息，如不可见、只能ADD、可以UPDATE、DELETE等等组合
 * tablename：哪张表
 * permissionType：操作权限 详情：PermissionType类
 * publicKey：公钥（账户概念，能具体到某个member，为*则代表所有节点，可以不用具体指定）
 * groupId：该权限归属于哪个group的，节点只能获取自己group的权限信息，不需要知道别的group的
 */

class Permission{
    var id:Long?=0L
    var createTime: Date?=null
    var updateTime: Date?=null
    /**
     * 哪张表
     */
    var tableName:String?=null
    /**
     * 操作权限，见PermissionType类
     */
    var permissionType:Byte?=null
    /**
     * 公钥（账户的概念，能具体到某个member，为*则代表所有节点，不具体指定某个）
     */
    var publicKey:String?=null
    /**
     * 该权限是归属于哪个group的。节点只需要获取自己group的权限信息，不需要知道别的group的
     */
    var groupId:String?=null
    var operation:Byte?=null

    /**
     * 如果通过repository 查询出的数据构造该类，则字段的顺序必须相同，否则字段数据将乱
     */
    constructor(id:Long,createTime: Date,updateTime: Date,groupId:String,operation:Byte,publicKey:String,tableName:String,permissionType:Byte){
        this.id = id
        this.createTime = createTime
        this.updateTime = updateTime
        this.groupId = groupId
        this.operation = operation
        this.publicKey = publicKey
        this.tableName = tableName
        this.permissionType = permissionType
    }
}