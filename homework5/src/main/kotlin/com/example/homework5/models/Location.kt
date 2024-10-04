package com.example.homework5.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TODO: исправить ошибку
 *
 * Использование `coordinates` приводило к
 *
 * `MissingKotlinParameterException: Instantiation of [simple type, class ...Location] value failed for JSON property
 * coordinates due to missing (therefore NULL) value for creator parameter coordinates which is a non-nullable type`
 */
@Serializable
data class Location(
    val slug: String,
    val name: String,
    val timezone: String,
//    @SerialName("coords") val coordinates: Coordinates,
    val language: String,
    val currency: String,
)

