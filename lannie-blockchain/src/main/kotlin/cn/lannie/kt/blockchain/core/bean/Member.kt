package cn.lannie.kt.blockchain.core.bean

import java.util.*


/**
 * 联盟的成员
 */
class Member {
    /**
     * 成员id，用于校验该客户是否合法，客户端启动时需要带着该值。一个公司可能有多个appId，相当于多个服务器节点
     */
    var appId: String? = null
    /**
     * 成员名
     */
    var name: String? = null
    /**
     * ip（可不设置，由该成员客户端启动后自行检测）
     */
    var ip: String? = null

    private val createTime: Date? = null

    private val updateTime: Date? = null

    override fun toString(): String {
        return "Member{" +
                "appId='" + appId + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", ip='" + ip + '\''.toString() +
                '}'.toString()
    }

}
