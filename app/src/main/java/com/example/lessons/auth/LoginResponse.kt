package com.example.lessons.auth

data class LoginResponse(
    val status: String,
    val token: String?,
    val message: String,
    val data: Map<String, Any>
)
