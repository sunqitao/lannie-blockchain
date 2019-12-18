package cn.lannie.kt.blockchainmanager.repository

import cn.lannie.kt.blockchainmanager.model.Permission
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
@Mapper
interface PermissionRepository{
    /**
     * 查询某个group的所有权限
     */
    @Select("select * from permission where group_id = #{groupId}")
    fun findByGroupId(groupId:String):List<Permission>
}