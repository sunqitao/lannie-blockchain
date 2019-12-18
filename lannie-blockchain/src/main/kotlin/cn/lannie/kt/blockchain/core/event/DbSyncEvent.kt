package cn.lannie.kt.blockchain.core.event

import org.springframework.context.ApplicationEvent

/**
 * 同步block到sqlite事件
 */
class DbSyncEvent(source: Any) : ApplicationEvent(source)
