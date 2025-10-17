package com.example.lessons.modules.backendApi.requests

import com.example.lessons.models.LessonRequestHours

data class LessonRequestRequest(
    val date: String,
    val hours: LessonRequestHours,
    val subject: String?,
    val schoolLevel: String?,
    val lessonPlace: String?,
    val comment: String?,
)
