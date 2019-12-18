package cn.lannie.kt.blockchain.core.event

import cn.lannie.kt.blockchain.block.Block
import org.springframework.context.ApplicationEvent


/**
 * 确定生成block的Event（添加到rocksDB，执行sqlite语句，发布给其他节点）
 */
class AddBlockEvent(block: Block) : ApplicationEvent(block)
