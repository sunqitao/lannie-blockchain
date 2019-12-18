package cn.lannie.kt.blockchainmanager.manager

import cn.lannie.kt.blockchainmanager.bean.MemberData
import cn.lannie.kt.blockchainmanager.model.Member
import cn.lannie.kt.blockchainmanager.repository.MemberRepository
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class MemberManager {
    @Resource
    lateinit var memberRepository:MemberRepository

    /**
     * 查询某成员的groupId
     */
    fun findGroupId(memberName:String):String{
        return memberRepository.findFirstByName(memberName)?.groupId!!
    }

    fun memberData(name:String,appId:String,ip:String):MemberData{
        var member:Member =  memberRepository.findFirstByAppId(appId)
        var memberData:MemberData?
        if(member==null ){
            memberData = MemberData(ArrayList() ,-1,"客户不存在")
        }else if(!member.name.equals(name)){
            memberData = MemberData(ArrayList() ,-2,"name错误")
        }else if(!member.ip.equals(ip)){
            memberData = MemberData(ArrayList() ,-3,"ip错误")
        }else{
            var groupId = findGroupId(name)
            memberData = MemberData(memberRepository.findByGroupId(groupId) ,0,"")
        }
        return memberData
    }
}