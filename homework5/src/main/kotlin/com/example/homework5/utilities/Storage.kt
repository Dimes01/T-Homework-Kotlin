package com.example.homework5.utilities

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class Storage<T> {
    private val storage = ConcurrentHashMap<Long, T>()
    private val logger = LoggerFactory.getLogger(Storage::class.java)

    fun getAll(): List<T> {
        logger.info("Method 'getAll': started")
        logger.info("Method 'getAll': finished")
        return storage.values.toList()
    }

    fun getById(id: Long): T? {
        logger.info("Method 'getById': started")
        val elem = storage[id]
        if (elem == null)
            logger.warn("Method 'getById': required element is not found")
        logger.info("Method 'getById': finished")
        return elem
    }

//    // Из-за того, что у Location такого же id, как у Category нет, то решил делегировать правило выбора наружу
//    fun getByRule(block: (T) -> Boolean): T? {
//        logger.info("Method 'getByRule': started")
//        val elem = storage.values.find { block(it) }
//        if (elem == null)
//            logger.warn("Method 'getByRule': required element is not found")
//        logger.info("Method 'getByRule': finished")
//        return elem
//    }

    fun save(id: Long, entity: T): T {
        logger.info("Method 'save': started")
        storage[id] = entity
        logger.debug("Method 'save': entity with id($id) saved")
        logger.info("Method 'save': finished")
        return entity
    }

    fun update(id: Long, entity: T): T? {
        logger.info("Method 'update': started")
        val result = if (storage.containsKey(id)) {
            storage[id] = entity
            logger.debug("Method 'update': result is entity with id($id)")
            entity
        } else {
            logger.debug("Method 'update': result is null")
            null
        }
        logger.info("Method 'update': finished")
        return result
    }

    fun delete(id: Long): Boolean {
        logger.info("Method 'delete': started")
        val result = storage.remove(id) != null
        if (result)
            logger.debug("Method 'delete': deleted entity with id($id)")
        logger.info("Method 'delete': finished")
        return result
    }
}