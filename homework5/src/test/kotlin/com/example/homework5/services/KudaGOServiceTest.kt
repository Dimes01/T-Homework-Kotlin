package com.example.homework5.services

import com.example.homework5.models.Category
import com.example.homework5.models.Location
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate


@ExtendWith(MockitoExtension::class)
class KudaGOServiceTest {
    @Mock
    private lateinit var restTemplate: RestTemplate

    @InjectMocks
    private lateinit var kudaGOService: KudaGOService

    @Test
    fun getCategories_responseIsNotNull_listOfCategory() {
        // Arrange
        val mockCategories = arrayOf(
            Category(1, "cat1", "category1"),
            Category(2, "cat2", "category2"))
        Mockito.`when`(restTemplate.getForObject(
            Mockito.anyString(),
            Mockito.eq(Array<Category>::class.java)
        )).thenReturn(mockCategories)

        // Act
        val categories = kudaGOService.getCategories()

        // Assert
        assertEquals(2, categories.size)
        assertEquals("category1", categories[0].name)
        assertEquals("category2", categories[1].name)
    }

    @Test
    fun getCategories_responseIsNull_emptyList() {
        // Arrange
        Mockito.`when`(restTemplate.getForObject(
            Mockito.anyString(),
            Mockito.eq(Array<Category>::class.java)
        )).thenReturn(null)

        // Act
        val categories = kudaGOService.getCategories()

        // Assert
        assertTrue(categories.isEmpty())
    }

    @Test
    fun getLocations_responseIsNotNull_listOfLocations() {
        // Arrange
        val mockLocations = arrayOf(
            Location("slug1", "name1", "timezone1", "language1", "currency1"),
            Location("slug2", "name2", "timezone2", "language2", "currency2")
        )
        Mockito.`when`(restTemplate.getForObject(
            Mockito.anyString(),
            Mockito.eq(Array<Location>::class.java)
        )).thenReturn(mockLocations)

        // Act
        val locations = kudaGOService.getLocations()

        // Assert
        assertEquals(2, locations.size)
        assertEquals("slug1", locations[0].slug)
        assertEquals("name1", locations[0].name)
        assertEquals("timezone1", locations[0].timezone)
    }

    @Test
    fun getLocations_responseIsNull_emptyList() {
        // Arrange
        Mockito.`when`(restTemplate.getForObject(
            Mockito.anyString(),
            Mockito.eq(Array<Location>::class.java)
        )).thenReturn(null)

        // Act
        val locations = kudaGOService.getLocations()

        // Assert
        assertTrue(locations.isEmpty())
    }

    @Test
    fun getCategories_throw_RestClientException() {
        // Arrange
        Mockito.`when`(restTemplate.getForObject(
            Mockito.anyString(),
            Mockito.eq(Array<Category>::class.java)
        )).thenThrow(RestClientException::class.java)

        // Act
        // Assert
        assertThrows<RestClientException> { kudaGOService.getCategories() }
    }

    @Test
    fun getLocations_throw_RestClientException() {
        // Arrange
        Mockito.`when`(restTemplate.getForObject(
            Mockito.anyString(),
            Mockito.eq(Array<Location>::class.java)
        )).thenThrow(RestClientException::class.java)

        // Act
        // Assert
        assertThrows<RestClientException> { kudaGOService.getLocations() }
    }
}