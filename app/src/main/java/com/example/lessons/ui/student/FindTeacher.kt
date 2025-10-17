package com.example.lessons.ui.student

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.lessons.modules.backendApi.data.dto.TeacherGeneralDataDto
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.ui.student.uiComponents.InputField
import com.example.lessons.ui.student.uiComponents.MultiSelect
import com.example.lessons.ui.student.uiComponents.NumberInputField
import com.example.lessons.viewModels.student.FindTeacherViewModel


@Composable
fun FindTeacher(navController: NavController, viewModel: FindTeacherViewModel = hiltViewModel()) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
        ) {
            val formData by viewModel.formData.collectAsState()
            val schoolLevelEnum by viewModel.schoolLevelEnum.collectAsState()
            val subjectsEnum by viewModel.subjectsEnum.collectAsState()
            val lessonPlaceEnum by viewModel.lessonPlaceEnum.collectAsState()

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    MultiSelect(
                        subjectsEnum,
                        "Subject",
                        null,
                        viewModel::updateFormFieldMap,
                        "subject",
                        formData.subject
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    MultiSelect(
                        schoolLevelEnum,
                        "School level",
                        null,
                        viewModel::updateFormFieldMap,
                        "schoolLevel",
                        formData.schoolLevel
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    MultiSelect(
                        lessonPlaceEnum,
                        "Lesson place",
                        null,
                        viewModel::updateFormFieldMap,
                        "lessonPlace",
                        formData.lessonPlace
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    InputField(
                        formData.moneyRate,
                        null,
                        viewModel::updateFormField,
                        "Max cost",
                        Icons.Default.Money,
                        "moneyRate",
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    NumberInputField(
                        formData.minLessonLength,
                        null,
                        viewModel::updateFormField,
                        "Min lesson length",
                        Icons.Default.Money,
                        "minLessonLength",
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    NumberInputField(
                        formData.maxLessonLength,
                        null,
                        viewModel::updateFormField,
                        "Max lesson length",
                        Icons.Default.Money,
                        "maxLessonLength",
                    )
                }
            }
        }
        val lazyPagingItems = viewModel.teacherFlow.collectAsLazyPagingItems()
        TeachersList(lazyPagingItems, navController)
    }
}

@Composable
fun TeachersList(teachers: LazyPagingItems<TeacherGeneralDataDto>, navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(key1 = teachers.loadState) {
        if (teachers.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error occurred: " + (teachers.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (teachers.loadState.refresh is LoadState.Loading) {
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
                    teachers.itemCount,
                    key = teachers.itemKey { it.id }
                ) { index ->
                    val teacher = teachers[index]
                    if (teacher != null) {
                        TeacherItem(
                            teacher = teacher,
                            navController = navController
                        )
                    }
                }
                item {
                    if (teachers.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherItem(
    teacher: TeacherGeneralDataDto,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(Screen.TeacherProfile.createRoute(teacher.id))
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Max)
        ) {
            teacher.decodeImage()?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Name: ${teacher.name} ${teacher.surname}"
                )
                Text(
                    text = "Email: ${teacher.email}"
                )
                Text(
                    text = "Phone number: ${teacher.phoneNumber}"
                )

                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    Column {
                        Text(
                            fontWeight = FontWeight.SemiBold,
                            text = "Self description: "
                        )
                        Text(
                            text = teacher.description
                        )
                    }

                }

                StarRating(teacher.ratingAvg, teacher.ratingCount)
            }
        }
    }
}

@Composable
fun StarRating(rating: Float, count: Int, maxStars: Int = 5) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalfStar = (rating - fullStars) >= 0.5
        val emptyStars = maxStars - fullStars - if (hasHalfStar) 1 else 0

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Full Star",
                tint = Color(0xFFFFD700)
            )
        }
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = "Half Star",
                tint = Color(0xFFFFD700)
            )
        }
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Empty Star",
                tint = Color.LightGray
            )
        }

        Text("$rating ($count)")
    }
}