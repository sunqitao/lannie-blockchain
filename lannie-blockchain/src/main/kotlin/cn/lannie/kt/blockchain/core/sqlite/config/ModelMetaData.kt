package cn.lannie.kt.blockchain.core.sqlite.config

import org.hibernate.SessionFactory
import org.hibernate.persister.entity.AbstractEntityPersister
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.Resource
import javax.persistence.EntityManagerFactory
import javax.persistence.Table


/**
 * 构建一个存放表名和model实体class的对应关系，如account_entity:AccountEntity.class
 *
 */
@Configuration
@AutoConfigureAfter(JpaConfiguration::class)
class ModelMetaData {

    @Bean(name = ["metaMap"])
    @Throws(ClassNotFoundException::class)
    fun metaMap(@Autowired @Qualifier(value = "localContainerEntityManagerFactoryBean")factory: EntityManagerFactory): Map<String, Class<*>> {
        if (factory.unwrap(SessionFactory::class.java) == null) {
            throw NullPointerException("factory is not a hibernate factory")
        }
        val sessionFactory = factory.unwrap(SessionFactory::class.java)
        //Map<String,ClassMetadata> getAllClassMetadata
        val metaMap = sessionFactory.metamodel
        val map = HashMap<String, Class<*>>()
        for (key in metaMap.entities) {
//            var entiryName = key.name.toLowerCase()
            var type = key.javaType
            var tableName = key.javaType.getAnnotation(Table::class.java).name.toLowerCase()
            map.put(tableName, type)
        }
        return map
    }

}
