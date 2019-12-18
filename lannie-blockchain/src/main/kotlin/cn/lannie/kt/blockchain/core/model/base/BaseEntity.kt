package cn.lannie.kt.blockchain.core.model.base

import cn.lannie.kt.blockchain.common.CommonUtil
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass


@MappedSuperclass
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var createTime: Long? = null

    var updateTime = CommonUtil().now
    /**
     * 最后操作人
     */
    var publicKey: String? = null

    override fun toString(): String {
        return "BaseEntity{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", publicKey='" + publicKey + '\''.toString() +
                '}'.toString()
    }
}