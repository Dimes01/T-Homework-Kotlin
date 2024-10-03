package com.example.homework5.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val slug: String,
    val name: String,
    val timezone: String,
    @SerialName("coords") val coordinates: Coordinates? = null,
    val language: String,
    val currency: String,
)

