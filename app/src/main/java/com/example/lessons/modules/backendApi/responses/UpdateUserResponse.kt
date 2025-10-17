package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.Rating
import com.example.lessons.models.User

data class UpdatedUserDataResponse(
    val user: User?,
    val data: Map<String, String>?,
    val canLeaveComment: Boolean?,
    val rate: Rating?
)

data class UpdateUserResponse(
    val status: String,
    val message: String?,
    val token: String?,
    val data: UpdatedUserDataResponse
)
