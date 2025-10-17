package com.example.lessons.modules.backendApi.responses

import com.example.lessons.models.Conversation
import com.example.lessons.models.LessonRequest

data class PostLessonRequestStatusResponse(
    val status: String,
    val message: String?,
    val data: PostLessonRequestStatusResponseData
)

data class PostLessonRequestStatusResponseData(
    val data: Map<String, String>?,
    val lessonRequest: LessonRequest,
    val conversation: Conversation?
)
