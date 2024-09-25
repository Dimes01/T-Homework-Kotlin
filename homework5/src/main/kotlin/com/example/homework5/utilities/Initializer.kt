package com.example.homework5.utilities

import com.example.homework5.models.Category
import com.example.homework5.models.Location
import com.example.homework5.services.KudaGOService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner

class Initializer(
    private val kudaGOService: KudaGOService,
    private val categoryStorage: Storage<Category>,
    private val locationStorage: Storage<Location>,
) : ApplicationRunner {
    private val logger = LoggerFactory.getLogger(Initializer::class.java)

    override fun run(args: ApplicationArguments?) {
        logger.info("Initializer and method 'run' are started")

        val categories = kudaGOService.getCategories()
        categories.forEach { categoryStorage.save(it) }
        logger.debug("Initializer: categories are loaded")

        val locations = kudaGOService.getLocations()
        locations.forEach { locationStorage.save(it) }
        logger.debug("Initializer: locations are loaded")

        logger.info("Initializer and method 'run' are finished")
    }
}