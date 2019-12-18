package cn.lannie.kt.blockchainmanager.bean

import cn.lannie.kt.blockchainmanager.base.bean.BaseData
import cn.lannie.kt.blockchainmanager.model.Permission

class PermissionData : BaseData {
    var permissions:List<Permission>?=null
    constructor(permissions:List<Permission>,code:Int,message:String ){
        this.permissions = permissions
        super.code=code
        super.message = message
    }
    constructor(){}
}