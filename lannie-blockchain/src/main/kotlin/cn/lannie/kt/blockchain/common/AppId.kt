package cn.lannie.kt.blockchain.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class AppId {
    /**
     * 节点的唯一标志
     */
    @Value("\${appId}")
    private val appId: String? = null
    /**
     * 该客户的唯一标志
     */
    @Value("\${name}")
    private val name: String? = null

    @PostConstruct
    fun init() {
        value = appId
        nameValue = name
    }

    companion object {
        var value: String? = null
        var nameValue: String? = null
    }
}
