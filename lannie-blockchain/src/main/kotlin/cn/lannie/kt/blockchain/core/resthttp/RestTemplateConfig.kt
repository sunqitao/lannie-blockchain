package cn.lannie.kt.blockchain.core.resthttp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate


/**
 */
@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(factory: ClientHttpRequestFactory): RestTemplate {
        return RestTemplate(factory)
    }

    @Bean
    fun simpleClientHttpRequestFactory(): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(5000)
        factory.setConnectTimeout(5000)
        return factory
    }
}