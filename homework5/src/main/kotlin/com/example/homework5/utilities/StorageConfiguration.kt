package com.example.homework5.utilities

import com.example.homework5.models.Category
import com.example.homework5.models.Location
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfiguration {
    private val categoryStorage = Storage<Category>()
    private val locationStorage = Storage<Location>()

    @Bean
    fun getCategoryStorage(): Storage<Category> = categoryStorage

    @Bean
    fun getLocationStorage(): Storage<Location> = locationStorage
}