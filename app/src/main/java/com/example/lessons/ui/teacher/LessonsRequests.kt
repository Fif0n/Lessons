package com.example.lessons.ui.teacher

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.lessons.modules.backendApi.data.dto.LessonRequestDto
import com.example.lessons.ui.student.uiComponents.InputField
import com.example.lessons.ui.student.uiComponents.SingleSelect
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.viewModels.teacher.LessonRequestsViewModel

@Composable
fun LessonsRequests(navController: NavController, viewModel: LessonRequestsViewModel = hiltViewModel()) {
    val isLoading by viewModel.isLoading.collectAsState()

    if (!isLoading) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
            ) {
                val formData by viewModel.formData.collectAsState()
                val statusEnum by viewModel.statusEnums.collectAsState()

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        SingleSelect(
                            statusEnum,
                            stringResource(com.example.lessons.R.string.status_label),
                            null,
                            viewModel::updateFormField,
                            "status",
                            formData.status
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        InputField(
                            formData.id,
                            null,
                            viewModel::updateFormField,
                            "ID",
                            Icons.Default.Abc,
                            "id",
                        )
                    }
                }
            }

            val lazyPagingItems = viewModel.lessonRequestFlow.collectAsLazyPagingItems()
            LessonRequestsList(lazyPagingItems, navController)
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun LessonRequestsList(lessonRequests: LazyPagingItems<LessonRequestDto>, navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(key1 = lessonRequests.loadState) {
        if (lessonRequests.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error occurred: " + (lessonRequests.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (lessonRequests.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items (
                    lessonRequests.itemCount,
                    key = lessonRequests.itemKey { it._id }
                ) { index ->
                    val lessonRequest = lessonRequests[index]
                    if (lessonRequest != null) {
                        LessonRequestItem(
                            lessonRequest,
                            navController
                        )
                    }
                }
                item {
                    if (lessonRequests.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun LessonRequestItem(
    lessonRequest: LessonRequestDto,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(Screen.LessonRequest.createRoute(lessonRequest._id))
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Max)
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                val student = lessonRequest.student

                Text(
                    text = "#${lessonRequest._id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(com.example.lessons.R.string.student_label),
                    fontSize = 16.sp
                )

                Text(
                    text = "${student.name} ${student.surname}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(com.example.lessons.R.string.subject_label_lesson),
                    fontSize = 16.sp
                )
                Text(
                    text = lessonRequest.subject,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(com.example.lessons.R.string.time_label_request),
                    fontSize = 16.sp
                )
                Text(
                    text = "${lessonRequest.dateFormatted} ${lessonRequest.hours.getHourRangeFormatted()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}