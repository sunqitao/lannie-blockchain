package cn.lannie.kt.blockchain.core.sqlite.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.sqlite.SQLiteDataSource
import javax.sql.DataSource


/**
 * 配置sqlite数据库的DataSource
 */
@Configuration
class DataSourceConfiguration {
    @Value("\${sqlite.dbName}")
    private val dbName: String? = null

    @Bean(destroyMethod = "", name = ["EmbeddeddataSource"])
    fun dataSource(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.sqlite.JDBC")
        dataSourceBuilder.url("jdbc:sqlite:" + dbName!!)
        dataSourceBuilder.type(SQLiteDataSource::class.java)
        return dataSourceBuilder.build()
    }

}
