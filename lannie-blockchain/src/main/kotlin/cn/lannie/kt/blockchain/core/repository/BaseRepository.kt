package cn.lannie.kt.blockchain.core.repository

import cn.lannie.kt.blockchain.core.model.base.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean


/**
 */
@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>
