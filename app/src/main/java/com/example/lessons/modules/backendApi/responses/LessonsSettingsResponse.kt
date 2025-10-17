package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.User

data class Enums(
    val subjects: Map<String, String>,
    val schoolLevels: Map<String, String>,
    val lessonPlaces: Map<String, String>
)

data class LessonsSettingsData(
    val user: User,
    val enums: Enums
)

data class LessonsSettingsResponse(
    val status: String,
    val data: LessonsSettingsData
)
