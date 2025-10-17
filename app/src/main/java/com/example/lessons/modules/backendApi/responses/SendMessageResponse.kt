package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.Conversation

data class SendMessageResponse(
    val status: String,
    val message: String,
    val data: SendMessageResponseData,
)

data class SendMessageResponseData(
    val conversation: Conversation
)
