package cn.lannie.kt.blockchainmanager.controller

import cn.lannie.kt.blockchainmanager.bean.MemberData
import cn.lannie.kt.blockchainmanager.manager.MemberManager
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger
import javax.annotation.Resource

@RestController
@RequestMapping("/member")
class MemberController : Logging{
    @Resource
    lateinit var memberManager: MemberManager

    @GetMapping
    fun member(name:String,appId:String,ip:String):MemberData{
        logger.debug("client get all manager members")
        return memberManager.memberData(name,appId,ip)
    }
}