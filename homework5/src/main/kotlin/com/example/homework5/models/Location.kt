package com.example.homework5.models

data class Location(
    val id: Long,
    val title: String,
    val slug: String,
    val address: String,
    val location: String,
    val site_url: String,
    val is_closed : Boolean,
)

