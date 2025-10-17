package com.example.lessons.ui.formDatas

data class UpdatePasswordFormData(
    val password: String = "",
    val passwordConfirm: String = "",
    val currentPassword: String = ""
)
