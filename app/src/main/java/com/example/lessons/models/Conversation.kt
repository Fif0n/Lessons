package com.example.lessons.models

data class Conversation(
    val _id: String,
    val lessonRequest: LessonRequest,
    val messages: List<ConversationMessage>,
    val createdAtAsString: String?,
    val createdAt: String
)

data class ConversationMessage(
    val user: User,
    val messageText: String,
    val timestamp: String
)
