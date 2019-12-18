package cn.lannie.kt.blockchain.core.model.convert

import cn.lannie.kt.blockchain.core.model.base.BaseEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import javax.annotation.Resource


/**
 * 表名和实体类的对应
 */
@Component
class ConvertTableName<T : BaseEntity> {
    @Qualifier(value = "metaMap")
    @Resource
    private val metaMap: Map<String, Class<T>>? = null

    /**
     * 根据表名获取class名
     * @return
     * 表对应的实体类
     */
    fun convertOf(tableName: String): Class<T> {
        return metaMap!![tableName]!!
    }

    /**
     * 根据类名取表名
     * @param clazz
     * 类名
     * @return
     * 表名
     */
    fun convertOf(clazz: Class<T>): String? {
        for (key in metaMap!!.keys) {
            if (metaMap[key] == clazz) {
                return key
            }
        }
        return null
    }
}
