package com.example.lessons.ui.student

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lessons.models.User
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.ui.student.uiComponents.InputField
import com.example.lessons.ui.student.uiComponents.SingleSelect
import com.example.lessons.viewModels.student.TeacherProfileViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeacherProfile(navController: NavController, viewModel: TeacherProfileViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val user by viewModel.userData.collectAsState()

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        val scrollState = rememberScrollState()
        user?.let { teacher ->
            Card(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "${teacher.name} ${teacher.surname}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    teacher.decodeImage()?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Contact data:",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Left
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Phone number: ${teacher.phoneNumber ?: "-"}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )
                            Text(
                                text = "Email: ${teacher.email}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Teacher description:",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Left
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = teacher.yourselfDescription ?: "-",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Teacher information:",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Left
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Taught subjects: ${teacher.subjectsFormatted()}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )

                            Text(
                                text = "Taught school level: ${teacher.schoolLevelsFormatted()}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )

                            Text(
                                text = "Lessons locations: ${teacher.lessonsPlacesFormatted()}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )

                            if (teacher.lessonPlace?.contains("online") == true) {
                                Text(
                                    text = "Online platform: ${teacher.lessonsPlatform}",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                                )
                            }

                            if (teacher.lessonPlace?.contains("onSite") == true) {
                                Text(
                                    text = "Teacher location: ${teacher.location?.address}",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                                )
                            }

                            Text(
                                text = "Lesson length: ${teacher.lessonLength} min",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )

                            Text(
                                text = "Lesson cost: ${teacher.lessonMoneyRate} $",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Available hours:",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Left
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (teacher.availableHours?.dayOfWeek != null) {
                                teacher.availableHours.dayOfWeek.forEach { day ->
                                    Text(
                                        text = "${day.dayName}: ${day.hoursFormatted()}",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (teacher.location != null) {
                        var showDialog by remember { mutableStateOf(false) }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Button(
                                    onClick = { showDialog = true },
                                ) {
                                    Text(
                                        text = "Show location on map",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        LocationDialog(showDialog, teacher) {
                            showDialog = false
                        }
                    }

                    var showRequestDialog by remember { mutableStateOf(false) }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Button(
                                onClick = { showRequestDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text(
                                    text = "Send request for lesson",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    LessonRequestDialog(showRequestDialog, teacher, viewModel) {
                        showRequestDialog = false
                    }

                    if (viewModel.canSendRating) {
                        var showPostRatingDialog by remember { mutableStateOf(false) }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Button(
                                    onClick = { showPostRatingDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) {
                                    Text(
                                        text = "Rate teacher",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        }

                        RateTeacherDialog(showPostRatingDialog, viewModel, teacher._id) {
                            showPostRatingDialog = false
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Button(
                                    onClick = { navController.navigate(Screen.TeacherRatings.createRoute(teacher._id)) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) {
                                    Text(
                                        text = "Opinions about teacher",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RateTeacherDialog(showDialog: Boolean, viewModel: TeacherProfileViewModel, teacherId: String, onDismiss: () -> Unit) {
    if (showDialog) {
        val ratingModel by viewModel.rating.collectAsState()
        val title = if (ratingModel == null) "Leave rating" else "Edit rating"

        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    var rating by remember { mutableFloatStateOf(ratingModel?.rate ?: 5.0f) }

                    RatingBar(
                        rating = rating,
                        onRatingChanged = { newRate ->
                            rating = newRate
                        }
                    )

                    Text(text = "Rating: $rating")

                    var rateText by remember { mutableStateOf(ratingModel?.text ?: "") }

                    OutlinedTextField(
                        rateText,
                        onValueChange = { rateText = it},
                        label = { Text("Description (optional)") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 2.dp)
                        ) {
                            Text("Cancel")
                        }
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                if (ratingModel == null) {
                                    viewModel.postRating(teacherId, rating, rateText, context)
                                } else {
                                    viewModel.editRating(ratingModel!!._id, rating, rateText, context)
                                }

                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 2.dp)
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    starCount: Int = 5,
    starSize: Dp = 40.dp,
    activeColor: Color = Color(0xFFFFD700),
    inactiveColor: Color = Color.LightGray,
) {
    Row {
        for (i in 1..starCount) {
            val icon = when {
                rating >= i -> Icons.Filled.Star
                rating >= i - 0.5f -> Icons.Filled.StarHalf
                else -> Icons.Outlined.Star
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (rating >= i - 0.5f) activeColor else inactiveColor,
                modifier = Modifier
                    .size(starSize)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val isLeftHalf = offset.x < size.width / 2
                            val newRating = if (isLeftHalf) i - 0.5f else i.toFloat()
                            if (newRating >= 1.0) {
                                onRatingChanged(newRating)
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun LocationDialog(showDialog: Boolean, user: User, onDismiss: () -> Unit) {
    if (showDialog) {
        val coordinates = user.location?.coordinates

        val latLngObject = if (coordinates != null && 0 in coordinates.indices && 1 in coordinates.indices) {
            LatLng(coordinates[0], coordinates[1])
        } else {
            null
        }

        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Location: ${user.location?.address}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    latLngObject?.let {
                        GoogleMap(
                            modifier = Modifier
                                .height(300.dp),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(it, 15f)
                            }
                        ) {
                            Marker(state = MarkerState(position = it))
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onDismiss() }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LessonRequestDialog(showDialog: Boolean, user: User, viewModel: TeacherProfileViewModel, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Send lesson request",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row {
                        Text("Lesson price: ${user.lessonMoneyRate} $")
                        Text("Lesson length: ${user.lessonLength}")
                    }

                    val context = LocalContext.current
                    val calendar = Calendar.getInstance()

                    var selectedDate by remember { mutableStateOf<String?>(null) }
                    var dayName by remember { mutableStateOf<String?>(null) }

                    fun getDayName(year: Int, month: Int, day: Int): String {
                        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                        val date = Calendar.getInstance().apply {
                            set(year, month, day)
                        }.time
                        return dateFormat.format(date)
                    }

                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                            val dayFormatted = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
                            val monthFormatted = if (month + 1 < 10) "0${month + 1}" else month + 1
                            selectedDate = "$year-${monthFormatted}-$dayFormatted"
                            dayName = getDayName(year, month, dayOfMonth)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )

                    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Pick a Date")
                        }

                        Text(
                            text = if (selectedDate != null) "Selected date: $selectedDate" else "Select Date",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (selectedDate != null && dayName != null) {
                            val times by viewModel.availableHours.collectAsState()
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.setAvailableHours(selectedDate!!, dayName!!)
                            }

                            times?.let {
                                HoursView(it, viewModel, selectedDate!!) { onDismiss() }
                            }

                            if (times?.isEmpty() == true) {
                                Text(
                                    text = "No hours available",
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Column {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HoursView(items: List<String>, viewModel: TeacherProfileViewModel, date: String, onDismiss: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        items.forEach { time ->
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                    .fillMaxWidth()
            ) {
                Text(
                    text = time,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp)
                        .clickable {
                            viewModel.setStartingHour(time)
                            showDialog = true
                        },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }

    val startingHour by viewModel.startingHour.collectAsState()

    if (startingHour != null) {
        ConfirmRequest(showDialog, viewModel, date, startingHour!!, onDismiss) {
            showDialog = false
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmRequest(
    showDialog: Boolean,
    viewModel: TeacherProfileViewModel,
    date: String,
    startingHour: String,
    onDismiss: () -> Unit,
    parentDismiss: () -> Unit
) {
    if (showDialog) {
        val localContext = LocalContext.current
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val commentError by viewModel.commentError.collectAsState()
                    val comment by viewModel.comment.collectAsState()

                    val subjectError by viewModel.subjectError.collectAsState()
                    val subject by viewModel.subject.collectAsState()

                    val schoolLevelError by viewModel.schoolLevelError.collectAsState()
                    val schoolLevel by viewModel.schoolLevel.collectAsState()

                    val lessonPlaceError by viewModel.lessonPlaceError.collectAsState()
                    val lessonPlace by viewModel.lessonPlace.collectAsState()

                    SingleSelect(
                        viewModel.subjectsEnum.value,
                        "Subject",
                        subjectError,
                        viewModel::updateFormField,
                        "subject",
                        subject
                    )

                    SingleSelect(
                        viewModel.schoolLevelEnum.value,
                        "School level",
                        schoolLevelError,
                        viewModel::updateFormField,
                        "schoolLevel",
                        schoolLevel
                    )

                    SingleSelect(
                        viewModel.lessonPlaceEnum.value,
                        "Lesson place",
                        lessonPlaceError,
                        viewModel::updateFormField,
                        "lessonPlace",
                        lessonPlace
                    )

                    InputField(
                        comment,
                        commentError,
                        viewModel::updateFormField,
                        "Comment (optional)",
                        Icons.Default.Textsms,
                        "comment"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 2.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                viewModel.sendLessonRequest(comment, date, startingHour, localContext) {
                                    onDismiss()
                                    parentDismiss()
                                }
                              },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 2.dp)
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    }
}