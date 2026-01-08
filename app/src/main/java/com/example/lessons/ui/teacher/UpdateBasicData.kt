package com.example.lessons.ui.teacher

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.utils.UiEvent
import com.example.lessons.viewModels.teacher.PanelViewModel
import com.example.lessons.viewModels.teacher.UpdateBasicDataViewModel
import kotlinx.coroutines.launch

@Composable
fun UpdateBasicData(navController: NavController, viewModel: UpdateBasicDataViewModel, panelViewModel: PanelViewModel) {
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val formData by viewModel.formData.collectAsState()

    val emailError by viewModel.emailError.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val surnameError by viewModel.surnameError.collectAsState()
    val phoneNumberError by viewModel.phoneNumberError.collectAsState()
    val yourselfDescriptionError by viewModel.yourselfDescriptionError.collectAsState()

    fun onFieldValueChange(field: String, value: String) {
        viewModel.updateFormField(field, value)
    }

    val context = LocalContext.current

    val uiEvent by viewModel.uiEvent.collectAsState()

    uiEvent?.let { event ->
        when (event) {
            is UiEvent.ShowMessage -> Toast.makeText(context, context.getString(event.messageResId), Toast.LENGTH_SHORT).show()
            is UiEvent.ShowText -> Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
        }
        viewModel.clearUiEvent()
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(com.example.lessons.R.string.update_basic_data_title),
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
                value = formData.name,
                onValueChange = { onFieldValueChange("name", it) },
                label = { Text(stringResource(com.example.lessons.R.string.name_label)) },
                isError = nameError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(com.example.lessons.R.string.name_label)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)

            )
            if (nameError != null) {
                Text(
                    text = nameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = formData.surname,
                onValueChange = { onFieldValueChange("surname", it) },
                label = { Text(stringResource(com.example.lessons.R.string.surname_label)) },
                isError = surnameError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(com.example.lessons.R.string.surname_label)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )
            if (surnameError != null) {
                Text(
                    text = surnameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = formData.email,
                onValueChange = { onFieldValueChange("email", it) },
                label = { Text(stringResource(com.example.lessons.R.string.email_hint)) },
                isError = emailError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(com.example.lessons.R.string.email_hint)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )
            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = formData.phoneNumber,
                onValueChange = { onFieldValueChange("phoneNumber", it) },
                label = { Text(stringResource(com.example.lessons.R.string.phone_label)) },
                isError = phoneNumberError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = stringResource(com.example.lessons.R.string.phone_label)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )
            if (phoneNumberError != null) {
                Text(
                    text = phoneNumberError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = formData.yourselfDescription,
                onValueChange = { onFieldValueChange("yourselfDescription", it) },
                label = { Text(stringResource(com.example.lessons.R.string.yourself_description_label)) },
                isError = yourselfDescriptionError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = stringResource(com.example.lessons.R.string.yourself_description_label)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(vertical = 10.dp),
            )
            if (yourselfDescriptionError != null) {
                Text(
                    text = yourselfDescriptionError!!,
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
                            viewModel.updateUserData(formData, panelViewModel)
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
                        text = stringResource(com.example.lessons.R.string.update_button),
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
                    text = stringResource(com.example.lessons.R.string.cancel),
                    fontSize = 16.sp
                )
            }

        }
    }
}