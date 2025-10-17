package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.AvailableHours

data class AvailableHoursResponse(
    val status: String,
    val data: AvailableHours?
)
