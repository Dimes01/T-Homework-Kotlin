package com.example.homework5.controllers;

import com.github.tomakehurst.wiremock.client.WireMock.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import com.github.tomakehurst.wiremock.client.WireMock.get as getFromWireMock
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get as getRequestBuilders

/**
 * Test class for the {@link com.example.homework5.controllers.CategoryController}
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerOldTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        stubFor(
            getFromWireMock(urlPathMatching("/public-api/v1.4/place-categories/"))
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    [
                        {"id": 1, "name": "Museums"},
                        {"id": 2, "name": "Parks"}
                    ]
                    """.trimIndent()
                )
            )
        )
    }

    @Test
    @Throws(Exception::class)
    fun getAllCategories() {
        mockMvc.perform(
            getRequestBuilders("/api/v1/places/categories")
        )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    }

    @Test
    @Throws(Exception::class)
    fun getCategoryById() {
        mockMvc.perform(
            getRequestBuilders(
                "/api/v1/places/categories/{0}",
                "0"
            )
        )
            .andExpect(status().isOk())
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun postCreateCategory() {
        val category = """
    {
        "id": 0,
        "slug": "",
        "name": ""
    }"""

        mockMvc.perform(
            post("/api/v1/places/categories/{0}", "0")
                .content(category)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun putUpdateCategory() {
        val category = """
    {
        "id": 0,
        "slug": "",
        "name": ""
    }"""

        mockMvc.perform(
            put("/api/v1/places/categories/{0}", "0")
                .content(category)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun deleteCategory() {
        mockMvc.perform(
            delete(
                "/api/v1/places/categories/{0}",
                "0"
            )
        )
            .andExpect(status().isOk())
            .andDo(print())
    }
}
