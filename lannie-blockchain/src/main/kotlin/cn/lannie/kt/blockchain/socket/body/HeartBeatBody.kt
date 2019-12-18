package cn.lannie.kt.blockchain.socket.body

/**
 */
@Deprecated("")
class HeartBeatBody : BaseBody {
    /**
     * text
     */
    var text: String? = null

    constructor() : super() {}

    constructor(text: String) : super() {
        this.text = text
    }

    override fun toString(): String {
        return "HeartBeatBody{" +
                "text='" + text + '\''.toString() +
                '}'.toString()
    }
}
