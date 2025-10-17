package com.example.lessons.ui.student

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.viewModels.student.CalendarViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(navController: NavController, viewModel: CalendarViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                    ),
            ) {
                val start by viewModel.startingDate.collectAsState()
                val end by viewModel.endingDate.collectAsState()

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Left Arrow",
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(Alignment.Start)
                                .padding(start = 4.dp)
                                .clickable {
                                    viewModel.previousWeek()
                                }
                        )

                        Text(
                            text = "$start - $end",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .weight(3f)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Right Arrow",
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(Alignment.End)
                                .padding(end = 4.dp)
                                .clickable {
                                    viewModel.nextWeek()
                                }
                        )
                    }
                }
            }

            val lessons by viewModel.lessons.collectAsState()

            if (!lessons.isNullOrEmpty()) {
                lessons?.get(0)?.requests?.let {
                    LazyColumn {
                        items(items = it, itemContent = { item ->
                            Row(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(8.dp)
                                        .clickable {
                                            navController.navigate(Screen.LessonRequest.createRoute(item._id))
                                        },
                                ) {
                                    Column(
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(
                                            text = "With: ${item.teacher.getFullName()}"
                                        )
                                        Text(
                                            text = "Subject: ${item.subject}"
                                        )
                                        Text(
                                            text = "Time: ${item.dateFormatted} ${item.hours.getHourRangeFormatted()}"
                                        )
                                    }
                                }
                            }
                        })
                    }
                }
            } else {
                Text(
                    text = "No lessons in this week",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}