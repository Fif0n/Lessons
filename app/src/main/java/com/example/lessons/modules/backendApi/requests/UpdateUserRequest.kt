package com.example.lessons.modules.backendApi.requests

import com.example.lessons.models.Location

data class UpdateUserRequest(
    val scenario: String,
    val name: String?,
    val surname: String?,
    val email: String?,
    val password: String?,
    val confirmPassword: String?,
    val subject: List<String>?,
    val schoolLevel: List<String>?,
    val lessonPlace: List<String>?,
    val location: Location?,
    val lessonsPlatform: String?,
    val lessonMoneyRate: Int?,
    val lessonsLength: Int?,
    val phoneNumber: String?,
)