package cn.lannie.kt.blockchain.core.sqlparser

import cn.lannie.kt.blockchain.core.model.base.BaseEntity

/**
 */
abstract class AbstractSqlParser<T : BaseEntity> {

    /**
     * 对象的类
     *
     * @return Class
     */
    internal abstract val entityClass: Class<*>

    /**
     * 解析sql的方法
     * @param operation 是什么操作
     * @param id 主键
     * @param entity 对象entity
     */
    internal abstract fun parse(operation: Byte, id: String, entity: T)
}
