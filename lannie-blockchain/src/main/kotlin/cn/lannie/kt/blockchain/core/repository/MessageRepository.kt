package cn.lannie.kt.blockchain.core.repository

import cn.lannie.kt.blockchain.core.model.MessageEntity

/**
 */
interface MessageRepository : BaseRepository<MessageEntity> {
    /**
     * 删除一条记录
     * @param messageId  messageId
     */
    fun deleteByMessageId(messageId: String)

    /**
     * 查询一个
     * @param messageId messageId
     * @return MessageEntity
     */
    fun findByMessageId(messageId: String): MessageEntity
}
