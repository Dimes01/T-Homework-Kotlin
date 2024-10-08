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
        if (location == null) {
            logger.warn("Method 'getLocationById': not found element")
            return ResponseEntity.badRequest().build()
        }
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
    fun putUpdateLocation(@PathVariable id: Long, @RequestBody location: Location): ResponseEntity<Location> {
        logger.info("Method 'putUpdateLocation' is started")
        val result = storage.update(id, location)
        if (!result) {
            logger.warn("Method 'putUpdateLocation': could not update element")
            return ResponseEntity.badRequest().build()
        }
        logger.info("Method 'putUpdateLocation' is finished")
        return ResponseEntity.ok(location)
    }

    @DeleteMapping("/{id}")
    fun deleteLocation(@PathVariable id: Long): ResponseEntity<Location> {
        logger.info("Method 'deleteLocation' is started")
        val deletingElem = storage.getById(id)
        val isDelete = storage.delete(id)
        if (!isDelete) {
            logger.warn("Method 'deleteLocation': could not delete element")
            return ResponseEntity.badRequest().build()
        }
        logger.info("Method 'deleteLocation' is finished")
        return ResponseEntity.ok(deletingElem)
    }
}