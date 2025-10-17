package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.Rating

data class RatingResponse(
    val status: String,
    val message: String?,
    val data: RatingResponseData
)

data class RatingResponseData(
    val rating: Rating,
    val data: Map<String, String>?
)
