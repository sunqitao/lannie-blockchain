package cn.lannie.kt.blockchain.core.model

import cn.lannie.kt.blockchain.core.model.base.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table


/**
 */
@Entity
@Table(name = "message")
class MessageEntity : BaseEntity() {
    /**
     * 内容
     */
    var content: String? = null
    /**
     * 目标用户
     */
    var target: String? = null
    /**
     * 来源
     */
    var origin: String? = null
    /**
     * 业务id
     */
    var messageId: String? = null

    override fun toString(): String {
        return "MessageEntity{" +
                "content='" + content + '\''.toString() +
                ", target='" + target + '\''.toString() +
                ", origin='" + origin + '\''.toString() +
                ", messageId='" + messageId + '\''.toString() +
                '}'.toString()
    }
}
