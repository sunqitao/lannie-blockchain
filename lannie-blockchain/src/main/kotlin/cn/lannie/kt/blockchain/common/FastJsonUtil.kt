package cn.lannie.kt.blockchain.common

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature

class FastJsonUtil {
    private val CONFIG: SerializeConfig

    private val FEATURES = arrayOf(SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    )

    init {
        CONFIG = SerializeConfig()
        CONFIG.put(java.util.Date::class.java, JSONLibDataFormatSerializer()) // 使用和json-lib兼容的日期输出格式
        CONFIG.put(java.sql.Date::class.java, JSONLibDataFormatSerializer()) // 使用和json-lib兼容的日期输出格式
    }


    fun toJSONString(`object`: Any): String {
        return JSON.toJSONString(`object`, CONFIG, *FEATURES)
    }

    fun toJSONNoFeatures(`object`: Any): String {
        return JSON.toJSONString(`object`, CONFIG)
    }


    fun toBean(text: String): Any {
        return JSON.parse(text)
    }

    fun <T> toBean(text: String, clazz: Class<T>): T {
        return JSON.parseObject(text, clazz)
    }

    /**
     * 转换为数组
     */
    fun <T> toArray(text: String, clazz: Class<T>): Array<Any?> {
        return JSON.parseArray(text, clazz).toTypedArray()
    }

    /**
     * 转换为List
     */
    fun <T> toList(text: String, clazz: Class<T>): List<T> {
        return JSON.parseArray(text, clazz)
    }

    /**
     * 将string转化为序列化的json字符串
     */
    fun textToJson(text: String): Any {
        return JSON.parse(text)
    }

    /**
     * json字符串转化为map
     */
    fun stringToCollect(s: String): Map<*, *> {
        return JSONObject.parseObject(s)
    }

    /**
     * 将map转化为string
     */
    fun collectToString(m: Map<*, *>): String {
        return JSONObject.toJSONString(m)
    }
}