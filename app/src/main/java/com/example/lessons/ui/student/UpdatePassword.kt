package com.example.lessons.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.viewModels.student.UpdatePasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun UpdatePassword(navController: NavController, viewModel: UpdatePasswordViewModel) {
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val formData by viewModel.formData.collectAsState()

    val passwordError by viewModel.passwordError.collectAsState()
    val passwordConfirmError by viewModel.passwordConfirmError.collectAsState()
    val currentPasswordError by viewModel.currentPasswordError.collectAsState()

    fun onFieldValueChange(field: String, value: String) {
        viewModel.updateFormField(field, value)
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Update password",
            fontSize = 36.sp,
            modifier = Modifier
                .padding(top = 60.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            OutlinedTextField(
                value = formData.currentPassword,
                onValueChange = { onFieldValueChange("currentPassword", it) },
                label = { Text("Old password") },
                isError = currentPasswordError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Old password"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (currentPasswordError != null) {
                Text(
                    text = currentPasswordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = formData.password,
                onValueChange = { onFieldValueChange("password", it) },
                label = { Text("New password") },
                isError = passwordError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "New password"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = formData.passwordConfirm,
                onValueChange = { onFieldValueChange("passwordConfirm", it) },
                label = { Text("Password confirm") },
                isError = passwordConfirmError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password confirm"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (passwordConfirmError != null) {
                Text(
                    text = passwordConfirmError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        loading = true
                        try {
                            viewModel.updatePassword(formData)
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
                        text = "Update",
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
                    text = "Cancel",
                    fontSize = 16.sp
                )
            }
        }
    }
}