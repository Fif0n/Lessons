package com.example.lessons.modules.backendApi.responses

data class LessonRequestEnumsResponse(
    val status: String,
    val data: LessonRequestEnumsResponseData
)

data class LessonRequestEnumsResponseData(
    val status: Map<String, String>
)
