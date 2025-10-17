package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.Conversation

data class ConversationResponse(
    val status: String,
    val message: String?,
    val data: ConversationResponseData
)

data class ConversationResponseData(
    val data: Map<String, String>?,
    val conversation: Conversation
)
