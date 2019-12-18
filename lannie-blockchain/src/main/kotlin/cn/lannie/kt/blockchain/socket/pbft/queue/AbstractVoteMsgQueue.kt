package cn.lannie.kt.blockchain.socket.pbft.queue

import cn.hutool.core.collection.CollectionUtil
import cn.lannie.kt.blockchain.common.timer.TimerManager
import cn.lannie.kt.blockchain.socket.pbft.msg.VoteMsg
import org.slf4j.LoggerFactory
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier


/**
 */
abstract class AbstractVoteMsgQueue : BaseMsgQueue() {
    /**
     * 存储所有的hash的投票集合
     */
    protected var voteMsgConcurrentHashMap = ConcurrentHashMap<String, MutableList<VoteMsg>>()
    /**
     * 存储本节点已确认状态的hash的集合，即本节点已对外广播过允许commit或拒绝commit的消息了
     */
    protected var voteStateConcurrentHashMap = ConcurrentHashMap<String, Boolean>()

    private val logger = LoggerFactory.getLogger(AbstractVoteMsgQueue::class.java)

    internal abstract fun deal(voteMsg: VoteMsg, voteMsgs: List<VoteMsg>)

    override fun push(voteMsg: VoteMsg) {
        val hash = voteMsg.hash!!
        var voteMsgs: MutableList<VoteMsg>? = voteMsgConcurrentHashMap[hash]
        if (CollectionUtil.isEmpty(voteMsgs)) {
            voteMsgs = ArrayList<VoteMsg>()
            voteMsgConcurrentHashMap[hash] = voteMsgs
        } else {
            //如果不空的情况下，判断本地集合是否已经存在完全相同的voteMsg了
            voteMsgs?.forEach {
                if (it.appId.equals(voteMsg.appId)) {
                    return
                }
            }
        }

        //添加进去
        voteMsgs?.add(voteMsg)
        //如果已经对该hash投过票了，就不再继续
        if (voteStateConcurrentHashMap[hash] != null) {
            return
        }

        deal(voteMsg, voteMsgs!!)
    }

    /**
     * 该方法用来确认待push阶段的下一阶段是否已存在已达成共识的Block
     *
     *
     * 譬如收到了区块5的Prepare的投票信息，那么我是否接受该投票，需要先校验Commit阶段是否已经存在number>=5的投票成功信息
     *
     * @param hash
     * hash
     * @return 是否超过
     */
    fun hasOtherConfirm(hash: String, number: Int): Boolean {
        //遍历该阶段的所有投票信息
        for (key in voteMsgConcurrentHashMap.keys) {
            //如果下一阶段存在同一个hash的投票，则不理会
            if (hash == key) {
                continue
            }
            //如果下一阶段的number比当前投票的小，则不理会
            if (voteMsgConcurrentHashMap[key]?.get(0)?.number!! < number) {
                continue
            }
            //只有别的>=number的Block已经达成共识了，则返回true，那么将会拒绝该hash进入下一阶段
            if (voteStateConcurrentHashMap[key] != null && voteStateConcurrentHashMap[key]!!) {
                return true
            }
        }
        return false
    }

    /**
     * 清理旧的block的hash
     */
    protected fun clearOldBlockHash(number: Int) {
        TimerManager().schedule(Supplier{
            for (key in voteMsgConcurrentHashMap.keys) {
                if (voteMsgConcurrentHashMap[key]?.get(0)?.number!! <= number) {
                    voteMsgConcurrentHashMap.remove(key)
                    voteStateConcurrentHashMap.remove(key)
                }
            }
            null
        }, 2000)
    }
}
