package com.example.lessons.viewModels.teacher

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material.icons.rounded.School
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.material.icons.rounded.History
import com.example.lessons.R
import com.example.lessons.auth.TokenManager
import com.example.lessons.models.User
import com.example.lessons.repositories.UserRepository
import com.example.lessons.ui.TeacherLoginActivity
import com.example.lessons.ui.teacher.navigation.NavigationItem
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.utils.LoggedUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PanelViewModel(private val repository: UserRepository, private val context: Context): ViewModel() {
    var isProfileActionNeeded = MutableStateFlow<Boolean>(true)
        private set

    var selectedItemIndex by mutableStateOf<Int?>(0)
        private set

    var user by mutableStateOf<User?>(null)
        private set

    private val _title = MutableStateFlow(context.getString(R.string.drawer_dashboard))
    val title: StateFlow<String> = _title.asStateFlow()

    init {
        viewModelScope.launch {
            user = repository.getLoggedUserData()

            if (user != null) {
                isProfileActionNeeded.value = !user!!.verified
            }
        }
    }

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
        val intent = Intent(context, TeacherLoginActivity::class.java)
        context.startActivity(intent)
    }

    fun getDrawerItems(): List<NavigationItem> {
        return listOf(
            NavigationItem(
                title = context.getString(R.string.drawer_dashboard),
                icon = Icons.Rounded.Dashboard,
                route = Screen.Dashboard.route
            ),
            NavigationItem(
                title = context.getString(R.string.drawer_calendar),
                icon = Icons.Rounded.CalendarMonth,
                route = Screen.Calendar.route
            ),
            NavigationItem(
                title = context.getString(R.string.drawer_lessons_requests_teacher),
                icon = Icons.Rounded.School,
                route = Screen.LessonsRequests.route
            ),
            NavigationItem(
                title = context.getString(R.string.drawer_opinions),
                icon = Icons.Rounded.PostAdd,
                route = Screen.OpinionsAboutMe.route
            ),
            NavigationItem(
                title = context.getString(R.string.drawer_conversations),
                icon = Icons.Rounded.QuestionAnswer,
                route = Screen.Conversations.route
            ),
            NavigationItem(
                title = context.getString(R.string.drawer_lessons_history),
                icon = Icons.Rounded.History,
                route = Screen.LessonsHistory.route
            ),
            NavigationItem(
                title = context.getString(R.string.drawer_logout),
                icon = Icons.Rounded.Logout,
                route = Screen.Logout.route
            ),
        )
    }

    fun checkVerification() {
        viewModelScope.launch {
            user = repository.getLoggedUserData()
            if (user != null) {
                isProfileActionNeeded.value = !user!!.verified
            }
        }
    }
}