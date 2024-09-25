package com.example.homework5.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val slug: String,
    val name: String
)
