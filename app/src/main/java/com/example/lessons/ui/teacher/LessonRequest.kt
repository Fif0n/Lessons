package com.example.lessons.ui.teacher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.lessons.viewModels.teacher.LessonRequestViewModel
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
            val student = it.student

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "Student:",
                        fontSize = 16.sp
                    )

                    Text(
                        text = "${student.name} ${student.surname}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

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
                        text = "Student contact:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Email:",
                        fontSize = 18.sp
                    )
                    Text(
                        text = student.email,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (student.phoneNumber != null) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Phone number:",
                            fontSize = 18.sp
                        )
                        Text(
                            text = student.phoneNumber,
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var showAcceptDialog by remember { mutableStateOf(false) }
                        var showRejectDialog by remember { mutableStateOf(false) }
                        var showSendMessageDialog by remember { mutableStateOf(false) }
                        var showSetLessonLinkDialog by remember { mutableStateOf(false) }

                        if (it.status == "Pending") {
                            Button(
                                onClick = { showAcceptDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32a852)),
                                border = BorderStroke(1.dp, Color.Black),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Accept request")
                            }

                            Button(
                                onClick = { showRejectDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFb51039)),
                                border = BorderStroke(1.dp, Color.Black),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reject request")
                            }
                        }

                        Button(
                            onClick = { showSendMessageDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCBD503)),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Send message", color = Color.Black)
                        }

                        if (it.lessonPlace == "Online" && it.status == "Accepted") {
                            Button(
                                onClick = { showSetLessonLinkDialog = true },
                                border = BorderStroke(1.dp, Color.Black),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Set Lesson link", color = Color.White)
                            }

                            SetLessonLink(showSetLessonLinkDialog, viewModel, it._id) {
                                showSetLessonLinkDialog = false
                            }
                        }

                        AcceptDialog(showAcceptDialog, viewModel, it._id) {
                            showAcceptDialog = false
                        }

                        RejectDialog(showRejectDialog, viewModel, it._id) {
                            showRejectDialog = false
                        }

                        SendMessage(showSendMessageDialog, viewModel, it._id, navController) {
                            showSendMessageDialog = false
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun AcceptDialog(showDialog: Boolean, viewModel: LessonRequestViewModel, id: String, onDismiss: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Are you sure you want to accept this lesson?",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    val context = LocalContext.current
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
                            onClick = { coroutineScope.launch {
                                viewModel.acceptLessonRequest(context, id) {
                                    onDismiss()
                                }
                            } },
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

@Composable
fun RejectDialog(showDialog: Boolean, viewModel: LessonRequestViewModel, id: String, onDismiss: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Are you sure you want to accept this lesson?",
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
                            onClick = { coroutineScope.launch {
                                viewModel.rejectLessonRequest(context, id, message) {
                                    onDismiss()
                                }
                            } },
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

@Composable
fun SetLessonLink(showDialog: Boolean, viewModel: LessonRequestViewModel, id: String, onDismiss: () -> Unit) {
    if (showDialog) {
        val coroutineScope = rememberCoroutineScope()
        var link by remember { mutableStateOf("") }

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
                        text = "Set lesson link",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    val context = LocalContext.current

                    OutlinedTextField(
                        value = link,
                        onValueChange = {link = it},
                        label = { Text("Lesson link") }
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
                                    viewModel.setLessonLink(context, id, link)
                                    onDismiss()
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