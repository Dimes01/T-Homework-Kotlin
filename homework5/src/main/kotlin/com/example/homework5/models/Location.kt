package com.example.homework5.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Long,
    val slug: String,
    val name: String,
    val timezone: String,
    @SerialName("coords") val coordinates: Coordinates,
    val language: String,
    val currency: String,
)

