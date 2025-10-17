package com.example.lessons.viewModels.student

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Class
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lessons.MainActivity
import com.example.lessons.auth.TokenManager
import com.example.lessons.models.User
import com.example.lessons.ui.student.navigation.NavigationItem
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.utils.LoggedUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PanelViewModel: ViewModel() {
    var selectedItemIndex by mutableStateOf<Int?>(0)
        private set

    var user by mutableStateOf<User?>(null)
        private set

    private val _title = MutableStateFlow("Dashboard")
    val title: StateFlow<String> = _title.asStateFlow()


    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun setSelectedIndex(index: Int?) {
        selectedItemIndex = index
    }

    fun logout(context: Context) {
        val tokenManager = TokenManager(context)
        tokenManager.clearJwt()
        LoggedUser.logOut()
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    fun getDrawerItems(): List<NavigationItem> {
        return listOf(
            NavigationItem(
                title = "Dashboard",
                icon = Icons.Rounded.Dashboard,
                route = Screen.Dashboard.route
            ),
            NavigationItem(
                title = "Find teacher",
                icon = Icons.Rounded.Search,
                route = Screen.FindTeacher.route
            ),
            NavigationItem(
                title = "My lesson requests",
                icon = Icons.Rounded.Send,
                route = Screen.LessonRequests.route
            ),
            NavigationItem(
                title = "Lessons calendar",
                icon = Icons.Rounded.Class,
                route = Screen.Calendar.route
            ),
            NavigationItem(
                title = "Conversations",
                icon = Icons.Rounded.ChatBubble,
                route = Screen.Conversations.route
            ),
            NavigationItem(
                title = "Lessons history",
                icon = Icons.Rounded.History,
                route = Screen.LessonsHistory.route
            ),
            NavigationItem(
                title = "Logout",
                icon = Icons.Rounded.Logout,
                route = Screen.Logout.route
            ),
        )
    }
}