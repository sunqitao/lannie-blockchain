package cn.lannie.kt.blockchain.socket.body

import cn.lannie.kt.blockchain.common.AppId
import cn.lannie.kt.blockchain.common.CommonUtil


/**
 *
 * 2017年3月27日 上午12:12:17
 */
open class BaseBody {

    /**
     * 消息发送时间
     */
    /**
     * @return the time
     */
    /**
     * @param time the time to set
     */
    var time: Long? = System.currentTimeMillis()
    /**
     * 每条消息的唯一id
     */
    var messageId = CommonUtil().generateUuid()
    /**
     * 回复的哪条消息
     */
    var responseMsgId: String? = null
    /**
     * 自己是谁
     */
    var appId = AppId.value

    override fun toString(): String {
        return "BaseBody{" +
                "time=" + time +
                ", messageId='" + messageId + '\''.toString() +
                ", responseMsgId='" + responseMsgId + '\''.toString() +
                '}'.toString()
    }
}
