package cn.lannie.kt.blockchain.common

import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class CommonUtil {
    val now: Long
        get() = System.currentTimeMillis()

    val localIp: String?
        get() {
            val inetAddress = localHostLANAddress
            return inetAddress?.hostAddress
        }

    /**
     * 获取本机ip地址
     */
    private// 遍历所有的网络接口
    // 在所有的接口下再遍历IP
    // 排除loopback类型地址
    // 如果是site-local地址，就是它了
    // site-local类型的地址未被发现，先记录候选地址
    // 如果没有发现 non-loopback地址.只能用最次选的方案
    val localHostLANAddress: InetAddress?
        get() {
            try {
                var candidateAddress: InetAddress? = null
                val ifaces = NetworkInterface.getNetworkInterfaces()
                while (ifaces.hasMoreElements()) {
                    val iface = ifaces.nextElement() as NetworkInterface
                    val inetAddrs = iface.inetAddresses
                    while (inetAddrs.hasMoreElements()) {
                        val inetAddr = inetAddrs.nextElement() as InetAddress
                        if (!inetAddr.isLoopbackAddress) {
                            if (inetAddr.isSiteLocalAddress) {
                                return inetAddr
                            } else if (candidateAddress == null) {
                                candidateAddress = inetAddr
                            }
                        }
                    }
                }
                return candidateAddress ?: InetAddress.getLocalHost()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }
}
