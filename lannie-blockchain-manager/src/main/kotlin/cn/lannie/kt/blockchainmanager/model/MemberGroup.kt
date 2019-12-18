package cn.lannie.kt.blockchainmanager.model

import java.util.*


/**
 * 联盟链，多个节点组成一个group，一个group为一个联盟链
 */

data class MemberGroup(var id:Long, var createTime: Date, var updateTime: Date,var name:String,var groupId:String)