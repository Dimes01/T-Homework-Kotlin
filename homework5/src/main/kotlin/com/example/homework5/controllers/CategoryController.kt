package com.example.homework5.controllers

import com.example.homework5.models.Category
import com.example.homework5.utilities.Storage
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/places/categories")
class CategoryController(
    private val storage: Storage<Category>,
) {
    private val logger = LoggerFactory.getLogger(CategoryController::class.java)

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<Category>> {
        logger.info("Method 'getAllCategories' is started")
        val categories = storage.getAll()
        logger.info("Method 'getAllCategories' is finished")
        return ResponseEntity.ok(categories)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Category> {
        logger.info("Method 'getCategoryById' is started")
        val category = storage.getById(id)
        logger.info("Method 'getCategoryById' is finished")
        return ResponseEntity.ok(category)
    }

    @PostMapping("/{id}")
    fun postCreateCategory(@PathVariable id: Long, @RequestBody category: Category): ResponseEntity<Category> {
        logger.info("Method 'postCreateCategory' is started")
        val elem = storage.save(id, category)
        logger.info("Method 'postCreateCategory' is finished")
        return ResponseEntity.ok(elem)
    }

    @PutMapping("/{id}")
    fun putUpdateCategory(@PathVariable id: Long, @RequestBody category: Category) {
        logger.info("Method 'putUpdateCategory' is started")
        val elem = storage.update(id, category)
        if (elem == null)
            logger.warn("Method 'putUpdateCategory': could not update element")
        logger.info("Method 'putUpdateCategory' is finished")
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long, @RequestBody category: Category) {
        logger.info("Method 'deleteCategory' is started")
        val isDelete = storage.delete(id, category)
        if (!isDelete)
            logger.warn("Method 'deleteCategory': could not delete element")
        logger.info("Method 'deleteCategory' is finished")
    }
}