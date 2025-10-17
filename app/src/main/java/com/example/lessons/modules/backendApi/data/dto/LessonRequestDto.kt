package com.example.lessons.modules.backendApi.data.dto

import com.example.lessons.models.LessonRequestHours
import com.example.lessons.models.User
import java.util.Date

data class LessonRequestDto(
    val _id: String,
    val date: Date,
    val hours: LessonRequestHours,
    val comment: String?,
    val moneyRate: Int,
    val status: String,
    val student: LessonRequestUserDto,
    val teacher: LessonRequestUserDto,
    val subject: String,
    val schoolLevel: String,
    val lessonPlace: String,
    val dateFormatted: String?,
)

data class LessonRequestUserDto(
    val name: String,
    val surname: String,
    val email: String
)