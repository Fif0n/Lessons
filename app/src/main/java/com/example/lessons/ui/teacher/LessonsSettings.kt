package com.example.lessons.ui.teacher

import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.lessons.models.Location
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.ui.teacher.uiComponents.InputField
import com.example.lessons.ui.teacher.uiComponents.MultiSelect
import com.example.lessons.viewModels.teacher.LessonsSettingViewModel
import com.example.lessons.viewModels.teacher.LessonsSettingsFormData
import com.example.lessons.viewModels.teacher.PanelViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import com.example.lessons.R
import com.example.lessons.utils.UiEvent

@RequiresApi(35)
@Composable
fun LessonsSettings(navController: NavController, viewModel: LessonsSettingViewModel, panelViewModel: PanelViewModel) {
    val loading by viewModel.isLoading.collectAsState()

    val formData by viewModel.formData.collectAsState()
    val schoolLevelEnum by viewModel.schoolLevelEnum.collectAsState()
    val subjectsEnum by viewModel.subjectsEnum.collectAsState()
    val lessonPlaceEnum by viewModel.lessonPlaceEnum.collectAsState()

    val schoolLevelError by viewModel.schoolLevelError.collectAsState()
    val subjectError by viewModel.subjectError.collectAsState()
    val lessonPlaceError by viewModel.lessonPlaceError.collectAsState()
    val lessonMoneyRateError by viewModel.lessonMoneyRateError.collectAsState()
    val lessonLengthError by viewModel.lessonLengthError.collectAsState()
    val lessonsPlatformError by viewModel.lessonsPlatformError.collectAsState()
    val addressError by viewModel.addressError.collectAsState()

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
            text = stringResource(R.string.update_lessons_settings_title),
            fontSize = 30.sp,
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
            MultiSelect(schoolLevelEnum, stringResource(R.string.school_level_label), schoolLevelError, viewModel::updateFormFieldMap, "schoolLevel", formData.schoolLevel)
            Spacer(modifier = Modifier.size(10.dp))

            MultiSelect(subjectsEnum, stringResource(R.string.subject_label), subjectError, viewModel::updateFormFieldMap, "subject", formData.subject)
            Spacer(modifier = Modifier.size(10.dp))

            MultiSelect(lessonPlaceEnum, stringResource(R.string.lesson_place_label), lessonPlaceError, viewModel::updateFormFieldMap, "lessonPlace", formData.lessonPlace)
            Spacer(modifier = Modifier.size(10.dp))

            if (formData.lessonPlace.containsKey("onSite")) {
                LocalizationSelect(viewModel, formData, addressError)
            }

            if (formData.lessonPlace.containsKey("online")) {
                InputField(
                    formData.lessonsPlatform,
                    lessonsPlatformError,
                    viewModel::updateFormField,
                    stringResource(R.string.lessons_online_platform_label),
                    Icons.Default.Web,
                    "lessonsPlatform"
                )
                Spacer(modifier = Modifier.size(10.dp))
            }

            InputField(
                formData.lessonMoneyRate,
                lessonMoneyRateError,
                viewModel::updateFormField,
                stringResource(R.string.lesson_money_rate_label),
                Icons.Default.Money,
                "lessonMoneyRate"
            )
            Spacer(modifier = Modifier.size(10.dp))

            InputField(
                formData.lessonLength,
                lessonLengthError,
                viewModel::updateFormField,
                stringResource(R.string.lesson_length_label_settings),
                Icons.Default.Timer,
                "lessonLength"
            )
            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    viewModel.updateLessonsData(formData, panelViewModel)
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
}

@RequiresApi(35)
@Composable
fun LocalizationSelect(viewModel: LessonsSettingViewModel, formData: LessonsSettingsFormData, error: String?) {
    var address by remember { mutableStateOf(formData.location?.address ?: "") }
    val latLngObject = formData.location?.let {
        val coordinates = it.coordinates
        if (coordinates != null && 0 in coordinates.indices && 1 in coordinates.indices) {
            LatLng(it.coordinates[0], it.coordinates[1])
        } else {
            null
        }
    }
    var latLng by remember { mutableStateOf<LatLng?>(latLngObject) }

    OutlinedTextField(
        value = address,
        onValueChange = { address = it },
        label = { Text(stringResource(R.string.address_input_label)) },
        isError = error != null,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.address_input_label)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    )
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    val context = LocalContext.current
    Button(
        onClick = {
            val location = viewModel.getLatLngFromAddress(address, context)
            location?.let {
                latLng = LatLng(it.first, it.second)
                val locationObject = Location(listOf(it.first, it.second), address)
                viewModel.updateLocation(locationObject)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.show_on_map_button))
    }

    Spacer(modifier = Modifier.size(10.dp))

    latLng?.let {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .height(300.dp),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(it, 15f)
            }
        ) {
            Marker(state = MarkerState(position = it))
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}