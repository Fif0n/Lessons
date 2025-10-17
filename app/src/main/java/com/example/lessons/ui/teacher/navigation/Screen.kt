package com.example.lessons.ui.teacher.navigation

sealed class Screen(val route: String) {
    object Dashboard: Screen("dashboard")
    object ProfileSettings: Screen("profile_settings")
    object Calendar: Screen("calendar")
    object LessonsRequests: Screen("lessons_requests")
    object AvailableHours: Screen("available_hours")
    object OpinionsAboutMe: Screen("opinions_about_me")
    object Conversations: Screen("conversations")
    object Conversation: Screen("conversation/{id}") {
        fun createRoute(id: String) = "conversation/$id"
    }
    object Logout: Screen("logout")
    object BasicData: Screen("basic_data")
    object Password: Screen("password")
    object LessonsSettings: Screen("lessons_settings")
    object LessonsHistory: Screen("lessons_history")
    object LessonRequest: Screen("lesson_request/{id}") {
        fun createRoute(id: String) = "lesson_request/$id"
    }
}