package cn.lannie.kt.blockchain.socket.body

import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg


/**
 * pbft投票
 */
class VoteBody : BaseBody {
    var voteMsg: VoteMsg? = null

    constructor() : super() {}

    constructor(voteMsg: VoteMsg) : super() {
        this.voteMsg = voteMsg
    }
}
