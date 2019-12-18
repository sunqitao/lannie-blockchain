package cn.lannie.kt.blockchain.block.db

import org.iq80.leveldb.DB
import org.iq80.leveldb.impl.Iq80DBFactory
import org.rocksdb.Options
import org.rocksdb.RocksDB
import org.rocksdb.RocksDBException
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.IOException

/**
 * 配置启用哪个db，部分Windows机器用不了rocksDB，可以选择levelDB
 */
@Configuration
class DbInitConfig {

    @Bean
    @ConditionalOnProperty("db.rocksDB")
    fun rocksDB(): RocksDB? {
        RocksDB.loadLibrary()

        val options = Options().setCreateIfMissing(true)
        try {
            return RocksDB.open(options, "./rocksDB")
        } catch (e: RocksDBException) {
            e.printStackTrace()
            return null
        }

    }

    @Bean
    @ConditionalOnProperty("db.levelDB")
    @Throws(IOException::class)
    fun levelDB(): DB {
        val options = org.iq80.leveldb.Options()
        options.createIfMissing(true)
        return Iq80DBFactory.factory.open(File("./levelDB"), options)
    }
}
