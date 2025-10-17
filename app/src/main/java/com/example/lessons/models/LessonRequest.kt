package com.example.lessons.models

data class LessonRequest(
    val _id: String,
    val student: User,
    val teacher: User,
    val date: String,
    val hours: LessonRequestHours,
    val comment: String?,
    val moneyRate: Int,
    var status: String,
    val subject: String,
    val schoolLevel: String,
    val lessonPlace: String,
    val dateFormatted: String?,
    val onlineLessonLink: String?,
)

data class LessonRequestHours(
    val startingHour: Hour,
    val endingHour: Hour,
) {
    fun getHourRangeFormatted(): String {
        return "${startingHour.hourFormatted()}:${startingHour.minuteFormatted()} - ${endingHour.hourFormatted()}:${endingHour.minuteFormatted()}"
    }
}