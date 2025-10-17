package com.example.lessons.modules.backendApi.requests

data class LessonRequestStatus(
    val newStatus: String,
    val message: String? = null
)
