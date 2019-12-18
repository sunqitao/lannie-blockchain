package cn.lannie.kt.blockchain.common.timer

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/**
 * 异步延时执行
 * 需要调用延时执行的生活可使用，避免在代码里面直接调用Thread.sleep()方法
 */
class TimerManager {
    private val executorService = Executors.newScheduledThreadPool(3)

    fun schedule(action:Supplier<*>,delay:Long){
        executorService.schedule(  { action.get() },delay,TimeUnit.MILLISECONDS)
    }

    fun scheduleAtFixedRate(action: Supplier<*>, initialDelay: Long, period: Long) {
        executorService.scheduleAtFixedRate( { action.get() }, initialDelay, period, TimeUnit.MILLISECONDS)
    }

    fun scheduleWithFixedDelay(action: Supplier<*>, initialDelay: Long, period: Long) {
        executorService.scheduleWithFixedDelay( { action.get() }, initialDelay, period, TimeUnit.MILLISECONDS)
    }
}