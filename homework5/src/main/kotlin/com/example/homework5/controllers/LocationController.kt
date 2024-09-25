package com.example.homework5.controllers

import com.example.homework5.models.Location
import com.example.homework5.utilities.Storage
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/locations")
class LocationController(
    private val storage: Storage<Location>,
) {
    private val logger = LoggerFactory.getLogger(CategoryController::class.java)

    @GetMapping
    fun getAllLocations(): ResponseEntity<List<Location>> {
        logger.info("Method 'getAllLocations' is started")
        val locations = storage.getAll()
        logger.info("Method 'getAllLocations' is finished")
        return ResponseEntity.ok(locations)
    }

    @GetMapping("/{id}")
    fun getLocationById(@PathVariable id: Long): ResponseEntity<Location> {
        logger.info("Method 'getLocationById' is started")
        val location = storage.getById(id)
        logger.info("Method 'getLocationById' is finished")
        return ResponseEntity.ok(location)
    }

    @PostMapping("/{id}")
    fun postCreateLocation(@PathVariable id: Long, @RequestBody location: Location): ResponseEntity<Location> {
        logger.info("Method 'postCreateLocation' is started")
        val elem = storage.save(id, location)
        logger.info("Method 'postCreateLocation' is finished")
        return ResponseEntity.ok(elem)
    }

    @PutMapping("/{id}")
    fun putUpdateLocation(@PathVariable id: Long, @RequestBody location: Location) {
        logger.info("Method 'putUpdateLocation' is started")
        val elem = storage.update(id, location)
        if (elem == null)
            logger.warn("Method 'putUpdateLocation': could not update element")
        logger.info("Method 'putUpdateLocation' is finished")
    }

    @DeleteMapping("/{id}")
    fun deleteLocation(@PathVariable id: Long, @RequestBody location: Location) {
        logger.info("Method 'deleteLocation' is started")
        val isDelete = storage.delete(id, location)
        if (!isDelete)
            logger.warn("Method 'deleteLocation': could not delete element")
        logger.info("Method 'deleteLocation' is finished")
    }
}