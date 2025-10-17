package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.LessonRequestHours

data class TakenHoursResponse(
    val status: String,
    val message: String?,
    val data: List<LessonRequestHours>,
)

