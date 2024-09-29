package com.example.homework5.utilities

import com.example.homework5.models.Category
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StorageTest {
    private lateinit var storage: Storage<Category>
    private val initElements: Array<Category> = arrayOf(
        Category(1, "slug1", "name1"),
        Category(2, "slug2", "name2"),
        Category(3, "slug3", "name3"),
    )

    private fun set_up(elements: Array<Category>) {
        storage = Storage()
        for (id in 1..elements.size)
            storage.save(id.toLong(), elements[id - 1])
    }


    private fun getAll_goodAndBadSituations(): Stream<Arguments> = Stream.of(
        Arguments.of(emptyArray<Category>()),
        Arguments.of(initElements)
    )

    @ParameterizedTest
    @MethodSource("getAll_goodAndBadSituations")
    fun getAll(elements: Array<Category>) {
        set_up(elements)
        assertEquals(elements.toList(), storage.getAll())
    }


    private fun getById_goodAndBadSituations(): Stream<Arguments> = Stream.of(
        Arguments.of(emptyArray<Category>(), 1, null),
        Arguments.of(initElements, 0, null),
        Arguments.of(initElements, 1, initElements[0])
    )

    @ParameterizedTest
    @MethodSource("getById_goodAndBadSituations")
    fun getById(elements: Array<Category>, id: Long, expected: Category?) {
        set_up(elements)
        assertEquals(expected, storage.getById(id))
    }


    private fun update_goodAndBadSituations(): Stream<Arguments> {
        val newElem = Category(1, "newSlug1", "newName1")
        return Stream.of(
            Arguments.of(emptyArray<Category>(), 1, newElem, false),
            Arguments.of(initElements, 0, newElem, false),
            Arguments.of(initElements, 1, newElem, true),
        )
    }

    @ParameterizedTest
    @MethodSource("update_goodAndBadSituations")
    fun update(elements: Array<Category>, id: Long, newElem: Category, expected: Boolean) {
        set_up(elements)
        assertEquals(expected, storage.update(id, newElem))
    }


    private fun delete_goodAndBadSituations(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(emptyArray<Category>(), 1, false),
            Arguments.of(initElements, 0, false),
            Arguments.of(initElements, 1, true),
        )
    }

    @ParameterizedTest
    @MethodSource("delete_goodAndBadSituations")
    fun delete(elements: Array<Category>, id: Long, expected: Boolean) {
        // Arrange
        set_up(elements)
        val expectedSizeAfter = if (expected) elements.size - 1 else elements.size

        // Act
        // Assert
        assertEquals(expected, storage.delete(id))
        assertEquals(expectedSizeAfter, storage.getAll().size)
    }
}