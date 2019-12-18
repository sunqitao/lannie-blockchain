package cn.lannie.kt.blockchain.block.db


/**
 * key-value型DB数据库操作接口
 */
interface DbStore {
    /**
     * 数据库key value
     *
     * @param key
     * key
     * @param value
     * value
     */
    fun put(key: String, value: String)

    /**
     * get By Key
     *
     * @param key
     * key
     * @return value
     */
    operator fun get(key: String): String?

    /**
     * remove by key
     *
     * @param key
     * key
     */
    fun remove(key: String)
}
