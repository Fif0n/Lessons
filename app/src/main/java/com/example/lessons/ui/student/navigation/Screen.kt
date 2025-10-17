package com.example.lessons.ui.student.navigation

sealed class Screen(val route: String) {
    object Dashboard: Screen("dashboard")
    object ProfileSettings: Screen("profile_settings")
    object Logout: Screen("logout")
    object BasicData: Screen("basic_data")
    object Password: Screen("password")
    object FindTeacher: Screen("find_teacher")
    object Calendar: Screen("calendar")
    object LessonsHistory: Screen("lessons_history")
    object TeacherProfile: Screen("teacher_profile/{id}") {
        fun createRoute(id: String) = "teacher_profile/$id"
    }
    object Conversations: Screen("conversations")
    object Conversation: Screen("conversation/{id}") {
        fun createRoute(id: String) = "conversation/$id"
    }
    object LessonRequests: Screen("lesson_requests")
    object LessonRequest: Screen("lesson_request/{id}") {
        fun createRoute(id: String) = "lesson_request/$id"
    }
    object TeacherRatings: Screen("teacher_ratings/{id}") {
        fun createRoute(id: String) = "teacher_ratings/$id"
    }
}