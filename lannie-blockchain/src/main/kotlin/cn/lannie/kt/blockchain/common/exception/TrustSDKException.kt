package cn.lannie.kt.blockchain.common.exception

import com.alibaba.fastjson.JSONObject

class TrustSDKException : Exception {

    var rtnCd: String
    var rtnMsg: String

    constructor(rtnCd:String,rtnMsg:String):super(rtnMsg){
          this.rtnCd = rtnCd
          this.rtnMsg = rtnMsg
    }
    constructor(rtnCd:String,rtnMsg:String,t:Throwable):super(rtnMsg,t){
        this.rtnCd = rtnCd
        this.rtnMsg = rtnMsg
    }

    override fun toString(): String {
        return JSONObject.toJSONString(this)
    }
    companion object {
        private val serialVersionUID = -4214831807802264420L
    }
}
