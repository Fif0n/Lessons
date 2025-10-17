package com.example.lessons.ui.teacher

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lessons.R
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.viewModels.teacher.ProfileSettingsViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileSettings(navController: NavController, viewModel: ProfileSettingsViewModel) {
    val actionsNeeded by viewModel.actionsNeeded.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (actionsNeeded.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .border(1.dp, Color(0xFFe8093e))
                    .background(Color(0xFFffa8bd))
                    .padding(20.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Actions needed to be verified: ", color = Color(0xFFe8093e))
                    actionsNeeded.forEach {
                        Text(text = it, color = Color(0xFFe8093e))
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .border(1.dp, Color(0xFF06c91d))
                    .background(Color(0xFF98f5a3))
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Your account is verified and students can see you", color = Color(0xFF06c91d))
            }
        }

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image by viewModel.selectedImageUri.collectAsState()

            if (image != null) {
                AsyncImage(
                    model = viewModel.selectedImageUri.value,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .background(Color.LightGray)
                        .padding(10.dp)
                        .clickable { showDialog = true },
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.avatar_placeholder),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .background(Color.LightGray)
                        .padding(10.dp)
                        .clickable { showDialog = true }
                )
            }


            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = viewModel.getFullName(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Click to change avatar",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

        }

        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
            color = Color.Gray
        )

        ListRow("Basic data", "Basic account data", Icons.Rounded.AccountCircle, Screen.BasicData.route, navController)
        ListRow("Password", "Change account password", Icons.Rounded.Password, Screen.Password.route, navController)
        ListRow("Lessons settings", "Settings about your taught lessons", Icons.Rounded.School, Screen.LessonsSettings.route, navController)
        ListRow("Available hours", "Set hours when you can make appointments", Icons.Rounded.EditCalendar, Screen.AvailableHours.route, navController)

        UploadImageDialog(showDialog, { showDialog = false }, viewModel)
    }
}

@Composable
fun ListRow(title: String, description: String, icon: ImageVector, route: String, navController: NavController) {
    Row(
        modifier = Modifier
            .clickable {
                navController.navigate(route)
            }
            .padding(20.dp)
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 20.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(30.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                )
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
        color = Color.Gray
    )
}

@Composable
fun UploadImageDialog(showDialog: Boolean, onDismiss: () -> Unit, viewModel: ProfileSettingsViewModel) {
    if (showDialog) {
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(viewModel.selectedImageUri.value)
        }

        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri }
        )

        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .height(350.dp)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Choose image", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFb31e9c))
                        ) {
                            Text("Pick photo")
                        }
                    }
                    val uploadImageError by viewModel.uploadImageError.collectAsState()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("Avatar is empty")
                        }

                        if (uploadImageError != null) {
                            Text(text = uploadImageError!!, color = Color.Red)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp)
                        ) {
                            Text("Close")
                        }

                        Button(
                            onClick = {
                                viewModel.uploadAvatar(selectedImageUri, onDismiss)
                              },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}