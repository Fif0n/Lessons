package com.example.lessons.ui.formDatas

data class FindTeacherFormData(
    val subject: Map<String, String> = emptyMap(),
    val schoolLevel: Map<String, String> = emptyMap(),
    val lessonPlace: Map<String, String> = emptyMap(),
    val moneyRate: String = "",
    val minLessonLength: String = "",
    val maxLessonLength: String = "",
)
