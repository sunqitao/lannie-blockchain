package cn.lannie.kt.blockchain.core.sqlite.config

import org.hibernate.boot.MetadataBuilder
import org.hibernate.boot.registry.StandardServiceRegistry
import org.hibernate.boot.spi.MetadataBuilderInitializer
import org.hibernate.engine.jdbc.dialect.internal.DialectResolverSet
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver
import org.jboss.logging.Logger


/**
 * SQLite工具
 */
class SQLiteMetadataBuilderInitializer : MetadataBuilderInitializer {

    override fun contribute(metadataBuilder: MetadataBuilder, serviceRegistry: StandardServiceRegistry) {
        val dialectResolver = serviceRegistry.getService(DialectResolver::class.java)

        if (dialectResolver !is DialectResolverSet) {
            logger.warnf("DialectResolver '%s' is not an instance of DialectResolverSet, not registering SQLiteDialect",
                    dialectResolver)
            return
        }

        (dialectResolver as DialectResolverSet).addResolver(resolver)
    }

    companion object {

        private val logger = Logger.getLogger(SQLiteMetadataBuilderInitializer::class.java)

        private val dialect = SQLiteDialect()

        private val resolver = { info: DialectResolutionInfo ->
            if (info.getDatabaseName() == "SQLite") {
                dialect
            }
            null
        }
    }
}