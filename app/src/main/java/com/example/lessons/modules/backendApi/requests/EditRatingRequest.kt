package com.example.lessons.modules.backendApi.requests

data class EditRatingRequest(
    val rate: Float,
    val text: String?
)
