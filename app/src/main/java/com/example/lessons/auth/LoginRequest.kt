package com.example.lessons.auth

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)
