package com.example.lessons.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.utils.LoggedUser
import com.example.lessons.viewModels.student.ConversationViewModel
import kotlinx.coroutines.launch

@Composable
fun Conversation(navController: NavController, viewModel: ConversationViewModel) {
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
            val conversation by viewModel.conversation.collectAsState()

            conversation?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                        ),
                ) {
                    Column {
                        Text(
                            text = stringResource(com.example.lessons.R.string.conversation_with, it.lessonRequest.teacher.getFullName()),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        )

                        Text(
                            text = stringResource(com.example.lessons.R.string.go_to_lesson_request),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Screen.LessonRequest.createRoute(it.lessonRequest._id))
                                },
                            textDecoration = TextDecoration.Underline
                        )
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))

                val scrollState = rememberScrollState()

                LaunchedEffect(conversation?.messages?.size) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(end = 6.dp, start = 6.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    val loggedUser = LoggedUser

                    it.messages.forEach { message ->
                        if (message.user._id == loggedUser.getId()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                width = 1.dp,
                                                color = Color.Black,
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .padding(10.dp)
                                            .align(Alignment.End)
                                    ) {
                                        Text(
                                            text = message.messageText,
                                            textAlign = TextAlign.Right,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Text(
                                        text = stringResource(com.example.lessons.R.string.created_at, message.timestamp),
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier
                                            .align(Alignment.End)
                                            .padding(top = 4.dp)
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFc33fd1), RoundedCornerShape(20.dp))
                                        .border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(10.dp)
                                        .align(Alignment.Start)
                                ) {
                                    Text(
                                        text = message.messageText,
                                        textAlign = TextAlign.Left,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                                Text(
                                    text = stringResource(com.example.lessons.R.string.created_at, message.timestamp),
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(top = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                var message by remember { mutableStateOf("") }

                Box(
                    modifier = Modifier.fillMaxWidth().background(Color.Gray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(4.dp)
                    ) {
                        TextField(
                            value = message,
                            onValueChange = { message = it },
                            placeholder = {
                                Text(
                                    text = stringResource(com.example.lessons.R.string.message_label),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                              },
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                )
                                .weight(3f),
                        )

                        val coroutineScope = rememberCoroutineScope()
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.sendMessage(message, it._id, context) {
                                        message = ""
                                    }
                                }

                            },
                            modifier = Modifier.weight(1f).fillMaxSize().padding(start = 4.dp)
                        ) {
                            Text(
                                text = stringResource(com.example.lessons.R.string.send_label),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}