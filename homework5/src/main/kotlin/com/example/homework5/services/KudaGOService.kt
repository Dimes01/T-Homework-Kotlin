package com.example.homework5.services

import com.example.homework5.models.Category
import com.example.homework5.models.Location
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate

@Service
class KudaGOService(
    private val restClient: RestClient,
) {
    private val logger = LoggerFactory.getLogger(KudaGOService::class.java)
    private val versionApi = 1.4
    private val baseUrl = "https://kudago.com/public-api/v$versionApi"

    fun getCategories(): List<Category> {
        logger.info("Method 'getCategories' started")

        val response = restClient.get()
            .uri("$baseUrl/place-categories/?lang=ru")
            .retrieve()
            .body(Array<Category>::class.java)
        if (response == null)
            logger.warn("Method 'getCategories': response for place categories is null")
        else
            logger.debug("Method 'getCategories': place categories are received")
        val placeCategories = response?.toList() ?: emptyList()

        logger.info("Method 'getCategories' finished")
        return placeCategories
    }

    fun getLocations(): List<Location> {
        logger.info("Method 'getLocations' started")
        val response = restClient.get()
            .uri("$baseUrl/locations/?lang=ru&fields=slug,name,timezone,coords,language,currency")
            .retrieve()
            .body(Array<Location>::class.java)
        if (response == null)
            logger.warn("Method 'getLocations': response is null")
        else
            logger.debug("Method 'getLocations': event categories are received")
        val locations = response?.toList() ?: emptyList()

        logger.info("Method 'getLocations' finished")
        return locations
    }
}