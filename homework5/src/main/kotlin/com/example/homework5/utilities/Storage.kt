package com.example.homework5.utilities

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * Возник вопрос по поводу рациональнсти данной реализации, а именно:
 *
 * **Стоит ли использовать `hashcode()` для определения `id` для хранилища?**
 *
 * До этого рассматривал варианты:
 * 1. Использовать, например, `AtomicLong` для генерации `id`, но тогда вставал вопрос о том,
 * как узнать `id` объекта, который передали в параметре методам `save()` и `update()`?
 * 2. Создать интерфейс, например, `ModelWithId` и наследовать `Category` и `Location` от него,
 * но как будто с основными сущностями этого делать не стоит.
 * 3. Классы-прослойки, которые наследуются от сущностей и реализуют интерфейс `ModelWithId`,
 * но это показалось муторно...
 */
class Storage<T> {
    private val storage = ConcurrentHashMap<Int, T>()
    private val logger = LoggerFactory.getLogger(Storage::class.java)

    private fun getIdByEntity(entity: T) = entity.hashCode()

    fun getAll(): List<T> {
        logger.info("Method 'getAll': started and finished")
        return storage.values.toList()
    }

    fun save(entity: T): T {
        logger.info("Method 'save': started")
        val id = getIdByEntity(entity)
        storage[id] = entity
        logger.debug("Method 'save': entity with id($id) saved")
        logger.info("Method 'save': finished")
        return entity
    }

    fun update(entity: T): T? {
        logger.info("Method 'update': started")
        val id = getIdByEntity(entity)
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

    fun delete(entity: T): Boolean {
        logger.info("Method 'delete': started")
        val id = getIdByEntity(entity)
        val result = storage.remove(id) != null
        if (result)
            logger.debug("Method 'delete': deleted entity with id($id)")
        logger.info("Method 'delete': finished")
        return result
    }
}