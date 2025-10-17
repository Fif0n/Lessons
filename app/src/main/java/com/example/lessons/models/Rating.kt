package com.example.lessons.models

data class Rating(
    val _id: String,
    val student: User,
    val teacher: User,
    val text: String?,
    val rate: Float,
    val timestamp: String
)
