package com.example.homework5.services

import com.example.homework5.models.Category
import com.example.homework5.models.Location
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class KudaGOService {
    private val logger = LoggerFactory.getLogger(KudaGOService::class.java)
    private val restTemplate = RestTemplate()
    private val versionApi = 1.4
    private val baseUrl = "https://kudago.com/public-api/v$versionApi"

    fun getCategories(): List<Category> {
        logger.info("Method 'getCategories' started")

        var response = restTemplate.getForObject("$baseUrl/event-categories/?lang=ru", Array<Category>::class.java)
        if (response == null)
            logger.warn("Method 'getCategories': response for event categories is null")
        else
            logger.debug("Method 'getCategories': event categories are received")
        val eventCategories = response?.toList() ?: emptyList()

        response = restTemplate.getForObject("$baseUrl/place-categories/?lang=ru", Array<Category>::class.java)
        if (response == null)
            logger.warn("Method 'getCategories': response for place categories is null")
        else
            logger.debug("Method 'getCategories': place categories are received")
        val placeCategories = response?.toList() ?: emptyList()

        logger.info("Method 'getCategories' finished")
        return eventCategories + placeCategories
    }

    fun getLocations(): List<Location> {
        logger.info("Method 'getLocations' started")
        val response = restTemplate.getForObject(
            "$baseUrl/locations/?lang=ru&fields=slug,name,timezone,coords,language,currency",
            Array<Location>::class.java)
        if (response == null)
            logger.warn("Method 'getLocations': response is null")
        else
            logger.debug("Method 'getLocations': event categories are received")
        val locations = response?.toList() ?: emptyList()

        logger.info("Method 'getLocations' finished")
        return locations
    }
}