package cn.lannie.kt.blockchain.core.sqlparser

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import cn.lannie.kt.blockchain.block.Operation
import cn.lannie.kt.blockchain.common.CommonUtil
import cn.lannie.kt.blockchain.core.model.MessageEntity
import cn.lannie.kt.blockchain.core.repository.MessageRepository
import org.springframework.stereotype.Service
import javax.annotation.Resource

/**
 * 解析语句入库的具体实现，Message表的
 */
@Service
class MessageSqlParser : AbstractSqlParser<MessageEntity>() {
    @Resource
    private val messageRepository: MessageRepository? = null

    override val entityClass: Class<*>
        get() = MessageEntity::class.java

    override fun parse(operation: Byte, messageId: String, entity: MessageEntity) {
        if (Operation.ADD === operation) {
            entity.createTime = CommonUtil().now
            entity.messageId = messageId
            messageRepository!!.save(entity)
        } else if (Operation.DELETE === operation) {
            messageRepository!!.deleteByMessageId(messageId)
        } else if (Operation.UPDATE === operation) {
            val messageEntity = messageRepository!!.findByMessageId(messageId)
            BeanUtil.copyProperties(entity, messageEntity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("id", "createTime"))
            messageRepository!!.save(messageEntity)
        }
    }

}
