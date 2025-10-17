package com.example.lessons.modules.backendApi.responses

data class NeededActions(
    val neededActions: List<String>?
)

data class ProfileDataResponse(
    val status: String,
    val data: NeededActions,
)
