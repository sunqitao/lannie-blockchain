package cn.lannie.kt.blockchain.core.sqlite.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.annotation.Resource
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

/**
 */
@Configuration
@EnableJpaRepositories(basePackages = ["cn.lannie.kt.blockchain.core.repository"], transactionManagerRef = "jpaTransactionManager", entityManagerFactoryRef = "localContainerEntityManagerFactoryBean")
@EnableTransactionManagement
class JpaConfiguration {
    @Resource
    private val jpaProperties: JpaProperties? = null

    @Bean
    @DependsOn("EmbeddeddataSource")
    @Autowired
    fun jpaTransactionManager(@Qualifier(value = "EmbeddeddataSource") dataSource: DataSource, entityManagerFactory: EntityManagerFactory): JpaTransactionManager {
        val jpaTransactionManager = JpaTransactionManager()
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory)
        jpaTransactionManager.setDataSource(dataSource)

        return jpaTransactionManager
    }

    @Bean(name = ["localContainerEntityManagerFactoryBean"])
    @DependsOn("EmbeddeddataSource")
    @Autowired
    internal fun localContainerEntityManagerFactoryBean(@Qualifier(value = "EmbeddeddataSource")
                                                            dataSource: DataSource, builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder.dataSource(dataSource)
                .packages("cn.lannie.kt.blockchain.core.model")
                .properties(getVendorProperties(dataSource))
                .build()
    }

    private fun getVendorProperties(dataSource: DataSource): Map<String, String> {
        return jpaProperties!!.properties
    }

}