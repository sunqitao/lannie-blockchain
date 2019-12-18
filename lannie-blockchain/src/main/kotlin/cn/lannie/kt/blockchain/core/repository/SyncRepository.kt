package cn.lannie.kt.blockchain.core.repository

import cn.lannie.kt.blockchain.core.model.SyncEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 */
interface SyncRepository : JpaRepository<SyncEntity, Long> {
    fun findTopByOrderByIdDesc(): SyncEntity
}
