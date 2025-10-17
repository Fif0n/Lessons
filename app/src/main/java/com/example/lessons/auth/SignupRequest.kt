package com.example.lessons.auth

data class SignupRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val role: String
)
