package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.User

data class UserApiResponse(
    val status: String,
    val data: UserData
)

data class UserData(
    val user: User
)
