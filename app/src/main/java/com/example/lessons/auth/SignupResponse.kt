package com.example.lessons.auth

data class SignupResponse(
    val status: String,
    val token: String?,
    val message: String,
    val data: Map<String, Any>
)
