package com.example.lessons.ui.student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.viewModels.student.LessonRequestViewModel
import kotlinx.coroutines.launch

@Composable
fun LessonRequest(navController: NavController, viewModel: LessonRequestViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        val lessonRequest by viewModel.lessonRequest.collectAsState()

        lessonRequest?.let {
            val scrollState = rememberScrollState()
            val teacher = it.teacher

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Column(
                       modifier = Modifier.clickable {
                            navController.navigate(Screen.TeacherProfile.createRoute(teacher._id))
                       }
                    ) {
                        Text(
                            text = "Teacher:",
                            fontSize = 16.sp
                        )

                        Text(
                            text = teacher.getFullName(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Subject:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = it.subject,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "School level:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = it.schoolLevel,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Lesson place:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = it.lessonPlace,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Time:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${it.dateFormatted} ${it.hours.getHourRangeFormatted()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Status:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = it.status,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (it.lessonPlace == "Online") {
                        Text(
                            text = "Lesson link:",
                            fontSize = 18.sp
                        )
                        Text(
                            text = it.onlineLessonLink ?: "-",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Text(
                        text = "Teacher contact:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Email:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = teacher.email,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (teacher.phoneNumber != null) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Phone number:",
                            fontSize = 18.sp
                        )
                        Text(
                            text = teacher.phoneNumber,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (!it.comment.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Additional information:",
                            fontSize = 18.sp
                        )
                        Text(
                            text = it.comment,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    var showSendMessageDialog by remember { mutableStateOf(false) }

                    Button(
                        onClick = { showSendMessageDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCBD503)),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Send message", color = Color.Black)
                    }

                    SendMessage(
                        showSendMessageDialog,
                        viewModel,
                        it._id,
                        navController
                    ) {
                        showSendMessageDialog = false
                    }
                }
            }
        }
    }
}

@Composable
fun SendMessage(showDialog: Boolean, viewModel: LessonRequestViewModel, id: String, navController: NavController, onDismiss: () -> Unit) {
    if (showDialog) {
        val coroutineScope = rememberCoroutineScope()
        var message by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Send message to student",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    val context = LocalContext.current

                    OutlinedTextField(
                        value = message,
                        onValueChange = {message = it},
                        label = { Text("Message") }
                    )

                    Row {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFb51039)),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                text = "Cancel",
                            )
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.sendMessage(context, id, message, navController)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32a852)),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text(
                                text = "Accept",
                            )
                        }
                    }
                }
            }
        }
    }
}