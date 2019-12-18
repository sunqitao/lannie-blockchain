package cn.lannie.kt.blockchain.socket.pbft.event

import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.springframework.context.ApplicationEvent


/**
 * 消息已被验证，进入到Prepare集合中
 */
class MsgPrepareEvent(source: VoteMsg) : ApplicationEvent(source)
