package cn.lannie.kt.blockchainmanager.model

import java.util.*


/**
 * 联盟成员
 *
 * appid : 节点id，用于校验改客户端是否合法，客户端启动时需要带着该值。一个公司可能又多个appId，相当于多个服务器节点
 * name：成员名
 * ip
 * groupId：属于哪个联盟链
 */
data class Member(var id:Long, var createTime: Date, var updateTime: Date, var appId:String, var name:String, var ip:String, var groupId:String)