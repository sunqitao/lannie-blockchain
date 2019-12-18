package cn.lannie.kt.blockchain.block.db

import org.iq80.leveldb.DB
import org.iq80.leveldb.impl.Iq80DBFactory.asString
import org.iq80.leveldb.impl.Iq80DBFactory.bytes
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import javax.annotation.Resource


/**
 * levelDB
 *
 */
@Component
@ConditionalOnProperty("db.levelDB")
class LevelDbStoreImpl : DbStore {
    @Resource
    private val db: DB? = null

    override fun put(key: String, value: String) {
        db!!.put(bytes(key), bytes(value))
    }

    override fun get(key: String): String {
        return asString(db!!.get(bytes(key)))
    }

    override fun remove(key: String) {
        db!!.delete(bytes(key))
    }
}
