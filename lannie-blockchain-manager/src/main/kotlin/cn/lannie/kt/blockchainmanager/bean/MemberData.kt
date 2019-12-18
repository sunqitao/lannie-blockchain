package cn.lannie.kt.blockchainmanager.bean

import cn.lannie.kt.blockchainmanager.base.bean.BaseData
import cn.lannie.kt.blockchainmanager.model.Member

class MemberData : BaseData{
    var members:List<Member>?=null
    constructor(members: List<Member>, code: Int, message: String){
        this.members = members
        super.code=code
        super.message = message
    }
}