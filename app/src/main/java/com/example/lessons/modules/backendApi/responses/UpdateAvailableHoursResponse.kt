package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.AvailableHours

data class UpdateResponse(
    val availableHours: AvailableHours?,
    val data: Map<String, String>?
)

data class UpdateAvailableHoursResponse(
    val status: String,
    val data: UpdateResponse
)
