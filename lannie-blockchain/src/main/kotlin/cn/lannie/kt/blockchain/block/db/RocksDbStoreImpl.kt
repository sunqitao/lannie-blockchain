package cn.lannie.kt.blockchain.block.db

import org.rocksdb.RocksDB
import org.rocksdb.RocksDBException
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import javax.annotation.Resource

/**
 * rocksDB对于存储接口的实现
 */
@Component
@ConditionalOnProperty("db.rocksDB")
class RocksDbStoreImpl : DbStore {
    @Resource
    private val rocksDB: RocksDB? = null

    override fun put(key: String, value: String) {
        try {
            rocksDB!!.put(key.toByteArray(Charsets.UTF_8), value.toByteArray(Charsets.UTF_8))
        } catch (e: RocksDBException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }


    override fun get(key: String): String? {
        try {
            val bytes = rocksDB!!.get(key.toByteArray(Charsets.UTF_8))
            return if (bytes != null) {
                String(bytes, Charsets.UTF_8)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    override fun remove(key: String) {
        try {
            rocksDB!!.delete(rocksDB!!.get(key.toByteArray(Charsets.UTF_8)))
        } catch (e: RocksDBException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

}
