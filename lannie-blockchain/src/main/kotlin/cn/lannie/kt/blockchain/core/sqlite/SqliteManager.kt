package cn.lannie.kt.blockchain.core.sqlite

import cn.lannie.kt.blockchain.ApplicationContextProvider
import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.block.InstructionBase
import cn.lannie.kt.blockchain.block.InstructionReverse
import cn.lannie.kt.blockchain.core.event.DbSyncEvent
import cn.lannie.kt.blockchain.core.manager.DbBlockManager
import cn.lannie.kt.blockchain.core.manager.SyncManager
import cn.lannie.kt.blockchain.core.model.SyncEntity
import cn.lannie.kt.blockchain.core.service.InstructionService
import cn.lannie.kt.blockchain.core.sqlparser.InstructionParser
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource


/**
 * 对sqlite数据库的操作（监听新增区块请求，执行对应的sql命令）
 *
 */
@Component
class SqliteManager {
    @Resource
    private val instructionParser: InstructionParser? = null
    @Resource
    private val syncManager: SyncManager? = null
    @Resource
    private val dbBlockManager: DbBlockManager? = null
    @Resource
    private val instructionService: InstructionService? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * sqlite同步，监听该事件后，去check当前已经同步到哪个区块了，然后继续执行之后的区块
     */
    @EventListener(DbSyncEvent::class)
    fun dbSync() {
        logger.info("开始执行导入区块到Sqlite操作")
        //查看同步到哪个区块了
        val syncEntity = syncManager!!.findLastOne()

        val block: Block
        if (syncEntity == null) {
            //从第一个开始
            block = dbBlockManager!!.firstBlock!!
            logger.info("正在导入第一个区块，hash为：" + block.hash)
        } else {
            val lastBlock = dbBlockManager!!.lastBlock
            //已经同步到最后一块了
            if (lastBlock!!.hash.equals(syncEntity!!.hash)) {
                logger.info("导入完毕")
                return
            }
            logger.info("正在导入区块，hash为：" + lastBlock.hash)
            val hash = syncEntity!!.hash
            block = dbBlockManager!!.getNextBlock(dbBlockManager!!.getBlockByHash(hash!!)!!)!!
        }
        execute(block)
        ApplicationContextProvider.publishEvent(DbSyncEvent(""))
    }

    /**
     * 根据一个block执行sql
     * 整个block一个事务
     *
     * @param block
     * block
     */
    @Transactional(rollbackFor = [Exception::class])
    fun execute(block: Block) {
        val instructions = block.blockBody!!.instructions
        //InstructionParserImpl类里面执行的是InstructionBase，需要转成InstructionBase
        for (instruction in instructions!!) {
            instruction.oldJson = (instruction.json!!)
        }
        doSqlParse(instructions)

        //保存已同步的进度
        val syncEntity = SyncEntity()
        syncEntity.hash = block.hash
        syncManager!!.save(syncEntity)
    }

    /**
     * 执行回滚一个block
     *
     * @param block
     * block
     */
    fun rollBack(block: Block) {
        val instructions = block.blockBody!!.instructions
        val size = instructions!!.size
        //需要对语句集合进行反转，然后执行和execute一样的操作
        val instructionReverses = ArrayList<InstructionReverse>(size)
        for (i in size - 1 downTo 0) {
            instructionReverses.add(instructionService!!.buildReverse(instructions.get(i)))
        }
        doSqlParse(instructionReverses)
    }

    private fun <T : InstructionBase> doSqlParse(instructions: List<T>) {
        for (instruction in instructions) {
            instructionParser!!.parse(instruction)
        }
    }

    /**
     * 测试block的代码是否能正确执行
     *
     * @param block block
     */
    @Transactional(rollbackFor = [Exception::class])
    fun tryExecute(block: Block) {
        execute(block)
    }
}
