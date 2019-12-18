package cn.lannie.kt.blockchain

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationEvent
import org.springframework.stereotype.Component

/**
 */
@Component
class ApplicationContextProvider : ApplicationContextAware {

    @Throws(BeansException::class)
    override fun setApplicationContext(ac: ApplicationContext) {
        context = ac
    }

    companion object {
        private var context: ApplicationContext? = null

        fun getApplicationContext(): ApplicationContext? {
            return context
        }

        fun <T> getBean(tClass: Class<T>): T {
            return context!!.getBean(tClass)
        }

        fun <T> getBean(name: String, tClass: Class<T>): T {
            return context!!.getBean(name, tClass)
        }

        fun publishEvent(event: ApplicationEvent) {
            context!!.publishEvent(event)
        }
    }
}
