package cn.lannie.kt.blockchain.socket.body

/**
 */
class BlockHash {
    var hash: String? = null
    var prevHash: String? = null
    var appId: String? = null

    constructor() {}

    constructor(hash: String, prevHash: String, appId: String) {
        this.hash = hash
        this.prevHash = prevHash
        this.appId = appId
    }
}
