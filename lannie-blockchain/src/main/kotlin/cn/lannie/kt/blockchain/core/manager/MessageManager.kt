package cn.lannie.kt.blockchain.core.manager

import cn.lannie.kt.blockchain.core.model.MessageEntity
import cn.lannie.kt.blockchain.core.repository.MessageRepository
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import javax.annotation.Resource

/**
 */
@Component
class MessageManager {
    @Resource
    private val messageRepository: MessageRepository? = null

    fun findAll(): List<MessageEntity> {
        return messageRepository!!.findAll()
    }

    fun findAllContent(): List<String> {
        return findAll().stream().map { it.content }.collect(Collectors.toList<String>())
    }

    fun findById(id: String): MessageEntity {
        return messageRepository!!.findByMessageId(id)
    }
}
