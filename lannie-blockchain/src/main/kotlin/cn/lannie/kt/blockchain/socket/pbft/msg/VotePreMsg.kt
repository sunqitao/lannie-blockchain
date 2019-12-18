package cn.lannie.kt.blockchain.socket.pbft.msg

import cn.lannie.kt.blockchain.block.Block

/**
 */
class VotePreMsg : VoteMsg() {
    var block: Block? = null
}
