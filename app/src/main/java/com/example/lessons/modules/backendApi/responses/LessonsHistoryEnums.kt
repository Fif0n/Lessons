package com.example.lessons.modules.backendApi.responses

data class LessonsHistoryEnums(
    val status: String,
    val data: LessonsHistoryEnumsResponseData
)

data class LessonsHistoryEnumsResponseData(
    val subjects: Map<String, String>
)
