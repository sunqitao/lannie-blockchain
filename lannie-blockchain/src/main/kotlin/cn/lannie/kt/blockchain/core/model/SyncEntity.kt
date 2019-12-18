package cn.lannie.kt.blockchain.core.model

import cn.lannie.kt.blockchain.common.CommonUtil
import javax.persistence.*


/**
 */
@Entity
@Table(name = "sync")
class SyncEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    /**
     * 已同步的区块hash
     */
    var hash: String? = null
    /**
     * 创建时间
     */
    var createTime = CommonUtil().now

    override fun toString(): String {
        return "AsyncEntity{" +
                "id=" + id +
                ", hash='" + hash + '\''.toString() +
                ", createTime=" + createTime +
                '}'.toString()
    }
}
