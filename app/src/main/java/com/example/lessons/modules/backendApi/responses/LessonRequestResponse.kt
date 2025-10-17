package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.LessonRequest

data class LessonRequestResponseData(
    val data: Map<String, String>?,
    val lessonRequest: LessonRequest?,
)

data class LessonRequestResponse(
    val status: String,
    val message: String?,
    val data: LessonRequestResponseData?,
)