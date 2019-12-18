package cn.lannie.kt.blockchainmanager.controller

import cn.lannie.kt.blockchainmanager.bean.PermissionData
import cn.lannie.kt.blockchainmanager.manager.PermissionManager
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@RequestMapping("/permission")
class PermissionController :Logging{

    @Resource
    lateinit var permissionManager: PermissionManager

    @GetMapping
    fun findGroupPermission(name:String):PermissionData{
        logger.debug("client get all permission by member name")
        return permissionManager.findGroupPermission(name)
    }

}