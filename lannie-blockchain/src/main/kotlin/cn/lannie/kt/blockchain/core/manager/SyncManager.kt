package cn.lannie.kt.blockchain.core.manager

import cn.lannie.kt.blockchain.core.model.SyncEntity
import cn.lannie.kt.blockchain.core.repository.SyncRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.annotation.Resource

/**
 */
@Service
class SyncManager {
    @Resource
    private val syncRepository: SyncRepository? = null

    fun findLastOne(): SyncEntity {
        return syncRepository!!.findTopByOrderByIdDesc()
    }

    fun save(syncEntity: SyncEntity): SyncEntity {
        return syncRepository!!.save(syncEntity)
    }

    fun findAll(pageable: Pageable): Any {
        return syncRepository!!.findAll(pageable)
    }

    fun deleteAll() {
        syncRepository!!.deleteAll()
    }
}
