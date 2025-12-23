package com.example.lessons.ui.teacher

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import com.example.lessons.R
import androidx.navigation.NavController
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.viewModels.teacher.DashboardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dashboard(navController: NavController, viewModel: DashboardViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IncomingLessons(viewModel, navController)

            }

            Column {
                EstimatedIncome(viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomingLessons(viewModel: DashboardViewModel, navController: NavController) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.lessons_incoming_week),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(3f)
                        .wrapContentWidth(Alignment.CenterHorizontally),
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
                                    navController.navigate(
                                        Screen.LessonRequest.createRoute(
                                            item._id
                                        )
                                    )
                                },
                        ) {
                            Column(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.with_label_format, item.student.getFullName())
                                )
                                Text(
                                    text = stringResource(R.string.subject_format, item.subject)
                                )
                                Text(
                                    text = stringResource(
                                        R.string.time_label_format,
                                        item.dateFormatted ?: "",
                                        item.hours.getHourRangeFormatted() ?: ""
                                    )
                                )
                            }
                        }
                    }
                })
            }
        }
    } else {
        Text(
            text = stringResource(R.string.no_lessons_in_week),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EstimatedIncome(viewModel: DashboardViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.estimated_income_current_month),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(3f)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        }
    }

    val estimatedIncome by viewModel.estimatedIncome.collectAsState()

    Column(
        modifier = Modifier
            .padding(8.dp),
    ) {
        Text(
            text = stringResource(R.string.current_estimated_income_format, (estimatedIncome?.currentIncome ?: 0).toString()),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.future_estimated_income_format, (estimatedIncome?.futureIncome ?: 0).toString()),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.total_estimated_income_month_format, (estimatedIncome?.estimatedIncome ?: 0).toString()),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
