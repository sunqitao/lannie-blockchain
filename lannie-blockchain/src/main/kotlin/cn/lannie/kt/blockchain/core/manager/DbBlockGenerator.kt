package cn.lannie.kt.blockchain.core.manager

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.block.check.CheckerManager
import cn.lannie.kt.blockchain.block.db.DbStore
import cn.lannie.kt.blockchain.common.Constants
import cn.lannie.kt.blockchain.core.event.AddBlockEvent
import cn.lannie.kt.blockchain.core.event.DbSyncEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.tio.utils.json.Json
import javax.annotation.Resource


/**
 * block的本地存储
 */
@Service
class DbBlockGenerator {
    @Resource
    private val dbStore: DbStore? = null
    @Resource
    private val checkerManager: CheckerManager? = null
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 数据库里添加一个新的区块
     *
     * @param addBlockEvent
     * addBlockEvent
     */
    @Order(1)
    @EventListener(AddBlockEvent::class)
    @Synchronized
    fun addBlock(addBlockEvent: AddBlockEvent) {
        logger.info("开始生成本地block")
        val block = addBlockEvent.getSource() as Block
        val hash = block.hash
        //如果已经存在了，说明已经更新过该Block了
        if (dbStore!!.get(hash!!) != null) {
            return
        }
        //校验区块
        if (checkerManager!!.check(block).code !== 0) {
            return
        }

        //如果没有上一区块，说明该块就是创世块
        if (block.blockHeader?.hashPreviousBlock == null) {
            dbStore!!.put(Constants.KEY_FIRST_BLOCK, hash)
        } else {
            //保存上一区块对该区块的key value映射
            dbStore!!.put(Constants.KEY_BLOCK_NEXT_PREFIX + block.blockHeader?.hashPreviousBlock, hash)
        }
        //存入rocksDB
        dbStore!!.put(hash, Json.toJson(block))
        //设置最后一个block的key value
        dbStore!!.put(Constants.KEY_LAST_BLOCK, hash)

        logger.info("本地已生成新的Block")

        //同步到sqlite
        sqliteSync()
    }

    /**
     * sqlite根据block信息，执行sql
     */
    @Async
    fun sqliteSync() {
        //开始同步到sqlite
        ApplicationContextProvider.publishEvent(DbSyncEvent(""))
    }
}
