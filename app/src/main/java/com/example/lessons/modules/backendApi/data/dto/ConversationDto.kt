package com.example.lessons.modules.backendApi.data.dto

import com.example.lessons.models.ConversationMessage
import com.example.lessons.models.LessonRequest

data class ConversationDto(
    val _id: String,
    val lessonRequest: LessonRequest,
    val messages: List<ConversationMessage>,
    val createdAt: String,
    val createdAtAsString: String?,
)
