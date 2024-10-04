package com.example.homework5.services

import com.example.homework5.models.Category
import com.example.homework5.models.Location
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class KudaGOService(
    private val restClient: RestClient,
) {
    private val logger = LoggerFactory.getLogger(KudaGOService::class.java)

    fun getCategories(): List<Category> {
        logger.info("Method 'getCategories' started")
        return restClient.get()
            .uri("/public-api/v1.4/place-categories?lang=ru")
            .retrieve()
            .body(Array<Category>::class.java)?.toList()
            .also { logger.info("Method 'getCategories' finished") }
            ?: run {
                logger.warn("Method 'getCategories': response for place categories is null")
                emptyList()
            }
    }

    fun getLocations(): List<Location> {
        logger.info("Method 'getLocations' started")
        return restClient.get()
            .uri("/public-api/v1.4/locations?lang=ru&fields=slug,name,timezone,coords,language,currency")
            .retrieve()
            .body(Array<Location>::class.java)?.toList()
            .also { logger.info("Method 'getLocations' finished") }
            ?: run {
                logger.warn("Method 'getLocations': response for locations is null")
                emptyList()
            }
    }
}