package com.example.lessons.ui.teacher.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lessons.modules.backendApi.AppModule
import com.example.lessons.repositories.UploadAvatarRepository
import com.example.lessons.repositories.UserRepository
import com.example.lessons.ui.teacher.AvailableHours
import com.example.lessons.ui.teacher.Calendar
import com.example.lessons.ui.teacher.Conversation
import com.example.lessons.ui.teacher.Conversations
import com.example.lessons.ui.teacher.LessonsHistory
import com.example.lessons.ui.teacher.Dashboard
import com.example.lessons.ui.teacher.LessonRequest
import com.example.lessons.ui.teacher.LessonsRequests
import com.example.lessons.ui.teacher.LessonsSettings
import com.example.lessons.ui.teacher.OpinionsAboutMe
import com.example.lessons.ui.teacher.ProfileSettings
import com.example.lessons.ui.teacher.UpdateBasicData
import com.example.lessons.ui.teacher.UpdatePassword
import com.example.lessons.utils.LoggedUser
import com.example.lessons.viewModels.teacher.AvailableHoursViewModel
import com.example.lessons.viewModels.teacher.CalendarViewModel
import com.example.lessons.viewModels.teacher.ConversationViewModel
import com.example.lessons.viewModels.teacher.DashboardViewModel
import com.example.lessons.viewModels.teacher.LessonRequestViewModel
import com.example.lessons.viewModels.teacher.LessonsSettingViewModel
import com.example.lessons.viewModels.teacher.PanelViewModel
import com.example.lessons.viewModels.teacher.ProfileSettingsViewModel
import com.example.lessons.viewModels.teacher.UpdateBasicDataViewModel
import com.example.lessons.viewModels.teacher.UpdatePasswordViewModel
import kotlinx.coroutines.launch

@RequiresApi(35)
@Composable
fun Navigation(navController: NavHostController, panelViewModel: PanelViewModel, content: Context) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(route = Screen.Dashboard.route) {
            val viewModel = DashboardViewModel(AppModule.provideApi(content),)
            Dashboard(navController, viewModel)
        }
        composable(route = Screen.ProfileSettings.route) {
            val userData = LoggedUser.getUserData()

            val viewModel = ProfileSettingsViewModel(
                userData["name"] ?: "",
                userData["surname"] ?: "",
                UserRepository(content),
                UploadAvatarRepository(content),
                content
            )
            ProfileSettings(navController, viewModel)
        }
        composable(route = Screen.Calendar.route) {
            val viewModel = CalendarViewModel(AppModule.provideApi(content))
            Calendar(navController, viewModel)
        }
        composable(route = Screen.LessonsRequests.route) {
            LessonsRequests(navController)
        }
        composable(route = Screen.AvailableHours.route) {
            val viewModel = AvailableHoursViewModel(UserRepository(content), content)
            AvailableHours(navController, viewModel, panelViewModel)
        }
        composable(route = Screen.OpinionsAboutMe.route) {
            OpinionsAboutMe(navController)
        }
        composable(route = Screen.Conversations.route) {
            Conversations(navController)
        }
        composable(route = Screen.Logout.route) {
            panelViewModel.logout(content)
        }
        composable(route = Screen.BasicData.route) {
            val viewModel = UpdateBasicDataViewModel(UserRepository(content), content)
            UpdateBasicData(navController, viewModel, panelViewModel)
        }
        composable(route = Screen.Password.route) {
            val viewModel = UpdatePasswordViewModel(UserRepository(content), content)
            UpdatePassword(navController, viewModel)
        }
        composable(route = Screen.LessonsSettings.route) {
            val viewModel = LessonsSettingViewModel(UserRepository(content))
            LessonsSettings(navController, viewModel, panelViewModel)
        }
        composable(route = Screen.LessonRequest.route) { backStackEntry ->
            val viewModel = LessonRequestViewModel(
                AppModule.provideApi(content),
                backStackEntry.arguments?.getString("id")
            )

            LessonRequest(navController, viewModel)
        }
        composable(route = Screen.Conversation.route) { backStackEntry ->
            val viewModel = ConversationViewModel(
                AppModule.provideApi(content),
                backStackEntry.arguments?.getString("id")
            )

            Conversation(navController, viewModel)
        }
        composable(route = Screen.LessonsHistory.route) {
            LessonsHistory(navController)
        }
    }
}

@RequiresApi(35)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Screen(viewModel: PanelViewModel, content: Context) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val selectedItemIndex by remember { derivedStateOf { viewModel.selectedItemIndex } }
    val isProfileActionNeeded by viewModel.isProfileActionNeeded.collectAsState()

    val title by viewModel.title.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.background(Color.LightGray)
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(com.example.lessons.R.string.nav_app_title),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    val drawerItems = viewModel.getDrawerItems()

                    HorizontalDivider(
                        color = Color.Black
                    )

                    drawerItems.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(26.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            },
                            selected = index == selectedItemIndex,
                            onClick = {
                                navController.navigate(item.route)
                                viewModel.setSelectedIndex(index)
                                viewModel.setTitle(item.title)
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                        )


                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

    ) {
        Scaffold(
            topBar = {
                TopBar(
                    viewModel,
                    navController,
                    title,
                    isProfileActionNeeded,
                    onOpen = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Navigation(navController, viewModel, content)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    viewModel: PanelViewModel,
    navController: NavHostController,
    title: String,
    isProfileActionNeeded: Boolean,
    onOpen: () -> Unit
) {
    val profileSettingsTitle = stringResource(com.example.lessons.R.string.nav_profile_settings_title)
    
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(com.example.lessons.R.string.nav_menu_desc),
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(28.dp)
                    .clickable {
                        onOpen()
                    }
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(com.example.lessons.R.string.nav_notifications_desc),
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .size(28.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate(Screen.ProfileSettings.route)
                        viewModel.setSelectedIndex(null)
                        viewModel.setTitle(profileSettingsTitle)
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(com.example.lessons.R.string.nav_account_desc),
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .size(28.dp)
                )
                if (isProfileActionNeeded) {
                    Text(
                        text = "!",
                        color = Color.Red,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = 10.dp, y = (-10).dp)
                    )
                }

            }
        },
    )
}
