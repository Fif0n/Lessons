package com.example.lessons.modules.backendApi.data.dto

import com.example.lessons.models.LessonRequest

data class CurrentWeekLessonsDto(
    // Aktualny rok i tydzień
    val _id: CurrentWeekLessonsDtoId,
    val requests: List<LessonRequest>
)

data class CurrentWeekLessonsDtoId(
    val year: Int,
    val week: Int
)
