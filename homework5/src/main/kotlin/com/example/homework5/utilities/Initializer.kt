package com.example.homework5.utilities

import com.example.homework5.aspects.LogExecutionTime
//import annotations.LogExecutionTime
import com.example.homework5.models.Category
import com.example.homework5.models.Location
import com.example.homework5.services.KudaGOService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class Initializer(
    private val kudaGOService: KudaGOService,
    private val categoryStorage: Storage<Category>,
    private val locationStorage: Storage<Location>,
) : ApplicationRunner {
    private val logger = LoggerFactory.getLogger(Initializer::class.java)
    private val idGenerator = AtomicLong(1)

    // На момент написания комментария данная аннотация работает только если находится в текущем проекте.
    // Если брать вариант из starter, то ничего работать не будет. В моём понимании это из-за бардака в структуре проектов.
    @LogExecutionTime
    override fun run(args: ApplicationArguments?) {
        logger.info("Initializer and method 'run' are started")

        val categories = kudaGOService.getCategories()
        categories.forEach { categoryStorage.save(it.id, it) }
        logger.debug("Initializer: categories are loaded")

        idGenerator.set(1)
        val locations = kudaGOService.getLocations()
        locations.forEach { locationStorage.save(idGenerator.getAndIncrement(), it) }
        logger.debug("Initializer: locations are loaded")

        logger.info("Initializer and method 'run' are finished")
    }
}