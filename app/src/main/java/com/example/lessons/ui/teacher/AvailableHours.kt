package com.example.lessons.ui.teacher

import android.annotation.SuppressLint
import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.lessons.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lessons.models.Day
import com.example.lessons.models.TimeRange
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.viewModels.teacher.AvailableHoursFormData
import com.example.lessons.viewModels.teacher.AvailableHoursViewModel
import com.example.lessons.viewModels.teacher.PanelViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun AvailableHours(navController: NavController, viewModel: AvailableHoursViewModel, panelViewModel: PanelViewModel) {
    val formData by viewModel.formData.collectAsState()

    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (formData.availableHours != null && formData.availableHours!!.dayOfWeek != null) {
            formData.availableHours!!.dayOfWeek?.forEach {
                WeekDayPanel(it, viewModel, formData, panelViewModel)
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    loading = true
                    try {
                        viewModel.updateAvailableHours(formData, panelViewModel)
                    } finally {
                        loading = false
                    }
                }
            },
            shape = RoundedCornerShape(30),
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.update_button),
                    fontSize = 16.sp
                )
            }
        }
        Button(
            onClick = { navController.navigate(Screen.ProfileSettings.route) },
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.cancel),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun WeekDayPanel(day: Day, viewModel: AvailableHoursViewModel, formData: AvailableHoursFormData, panelViewModel: PanelViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dayNamePicking by remember { mutableStateOf(day.dayName) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp, top = 12.dp)
            .background(Color(0xFFEBC034), shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day.dayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row {
                        Button(
                            onClick = {
                                showDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059649)),
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text(stringResource(R.string.add))
                        }

                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFb5050b))
                        ) {
                            Text(stringResource(R.string.delete))
                        }
                    }

                }
            }

            Text(
                text = stringResource(com.example.lessons.R.string.available_hours_label) + day.hoursFormatted(),
            )
        }

    }

    TimePickerDialog(showDialog, { showDialog = false }, dayNamePicking, viewModel)
    DeleteHourDialog(showDeleteDialog, { showDeleteDialog = false}, day, viewModel, formData, panelViewModel)
}

@Composable
fun TimePickerDialog(showDialog: Boolean, onDismiss: () -> Unit, dayName: String, viewModel: AvailableHoursViewModel) {
    var startingHour by remember { mutableStateOf<Int?>(null) }
    var endingHour by remember { mutableStateOf<Int?>(null) }
    var startingMinute by remember { mutableStateOf<Int?>(null) }
    var endingMinute by remember { mutableStateOf<Int?>(null) }

    val hoursError by viewModel.hoursError.collectAsState()

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .height(230.dp)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.choose_time_for_day, dayName), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            TimePicker({hour, minute ->
                                startingHour = hour
                                startingMinute = minute
                            }, startingHour, startingMinute)
                        }

                        Column {
                            TimePicker({hour, minute ->
                                endingHour = hour
                                endingMinute = minute
                            }, endingHour, endingMinute, false)

                        }

                    }

                    hoursError?.let { Text(text = it, color = Color.Red) }

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                onDismiss()
                                startingHour = null
                                startingMinute = null
                                endingHour = null
                                endingMinute = null
                                viewModel.setError(null)
                              },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.close_dialog))
                        }
                        
                        Button(
                            onClick = {
                                if (startingHour != null && startingMinute != null && endingHour != null && endingMinute != null) {
                                    val result = viewModel.setHours(dayName, startingHour!!, startingMinute!!, endingHour!!, endingMinute!!)
                                    if (result) {
                                        startingHour = null
                                        startingMinute = null
                                        endingHour = null
                                        endingMinute = null
                                        onDismiss()
                                    }
                                } else {
                                    onDismiss()
                                }
                              },
                            colors = ButtonDefaults.buttonColors(Color(0xFF57B9FF)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.add))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteHourDialog(showDialog: Boolean, onDismiss: () -> Unit, day: Day, viewModel: AvailableHoursViewModel, formData: AvailableHoursFormData, panelViewModel: PanelViewModel) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(text = stringResource(R.string.delete_hours_dialog), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    day.hours?.forEachIndexed { index, hour ->
                        Row(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clickable {
                                    showConfirmDialog = true
                                }
                                .background(Color(0xFFEBC034))
                                .padding(top = 10.dp, bottom = 10.dp)
                                .fillMaxWidth()

                        ) {
                            Text(
                                hour.hourRangeFormatted()
                            )
                        }
                        ConfirmDelete(showConfirmDialog, { showConfirmDialog = false }, {
                            viewModel.deleteHour(day, index)
                            viewModel.updateAvailableHours(formData, panelViewModel)
                        })
                    }
                }
            }

        }
    }
}

@Composable
fun ConfirmDelete(showDialog: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.confirm_action)) },
            text = { Text(stringResource(R.string.confirm_delete_hour)) },
            confirmButton = {
                TextButton(onClick = { onConfirm(); onDismiss() }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimePicker(onTimeSelected: (Int, Int) -> Unit, selectedHour: Int?, selectedMinute: Int?, startingHour: Boolean = true) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute -> onTimeSelected(hourOfDay, minute) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    var timeFormatted by remember { mutableStateOf("-") }
    if (selectedHour != null && selectedMinute != null) {
        timeFormatted = String.format("%02d:%02d", selectedHour, selectedMinute)
    }

    Text(
        text = if (startingHour) stringResource(R.string.starting_hour, timeFormatted) else stringResource(R.string.ending_hour, timeFormatted),
        modifier = Modifier.padding(bottom = 10.dp)
    )
    Button(
        onClick = { timePickerDialog.show()  },
        shape = RoundedCornerShape(30),
    ) {
        Text(
            text = stringResource(R.string.choose_time_button),
            fontSize = 16.sp
        )
    }
}