package com.example.lessons.modules.backendApi.requests

data class PostRatingRequest(
    val teacherId: String,
    val rate: Float,
    val text: String?
)
