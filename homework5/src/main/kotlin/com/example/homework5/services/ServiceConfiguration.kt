package com.example.homework5.services

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class ServiceConfiguration {
    @Bean
    fun getRestClient(): RestClient = RestClient.builder().baseUrl("https://kudago.com/public-api/v1.4").build()
}