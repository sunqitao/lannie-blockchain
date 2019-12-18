package cn.lannie.kt.blockchainmanager.manager

import cn.lannie.kt.blockchainmanager.bean.PermissionData
import cn.lannie.kt.blockchainmanager.repository.PermissionRepository
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class PermissionManager {
    @Resource
    lateinit var permissionRepository : PermissionRepository
    @Resource
    lateinit var memberManager: MemberManager

    /**
     * 查询某个联盟内的所有权限
     */
    fun findGroupPermission(memberName:String):PermissionData{
        var groupId = memberManager.findGroupId(memberName)
        var permissions = permissionRepository.findByGroupId(groupId)
        return PermissionData(permissions,0,"")
    }
}