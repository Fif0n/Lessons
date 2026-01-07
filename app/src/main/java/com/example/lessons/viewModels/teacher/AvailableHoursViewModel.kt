package com.example.lessons.viewModels.teacher

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.R
import com.example.lessons.models.AvailableHours
import com.example.lessons.models.Day
import com.example.lessons.models.Hour
import com.example.lessons.models.TimeRange
import com.example.lessons.modules.backendApi.responses.AvailableHoursResponse
import com.example.lessons.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AvailableHoursViewModel(private val repository: UserRepository, val context: Context): ViewModel() {
    private val _formData = MutableStateFlow<AvailableHoursFormData>(AvailableHoursFormData())
    val formData: StateFlow<AvailableHoursFormData> = _formData

    var hoursError = MutableStateFlow<String?>(null)
        private set

    init {
        viewModelScope.launch {
            val response = repository.getAvailableHours()
            setResponseData(response)
        }
    }

    private fun setDefaultAvailableHours() {
        val defaultData = mutableListOf<Day>()
        for (i in 1..7) {
            defaultData.add(Day(i, emptyList()))
        }

        _formData.value = _formData.value.copy(
            availableHours = AvailableHours(defaultData)
        )
    }

    private fun setResponseData(response: AvailableHoursResponse?) {
        if (response?.data != null && response.data.dayOfWeek?.isNotEmpty() == true) {
            _formData.value = _formData.value.copy(
                availableHours = response.data
            )
        } else {
            setDefaultAvailableHours()
        }
    }

    fun setHours(dayNumber: Int, startingHour: Int, startingMinute: Int, endingHour: Int, endingMinute: Int): Boolean {
        if (startingHour > endingHour) {
            setError(context.getString(R.string.starting_time_greater_error))
            return false
        }

        if (startingHour == endingHour && startingMinute > endingMinute) {
            setError(context.getString(R.string.starting_time_greater_error))
            return false
        }

        _formData.update { currentData ->
            currentData.copy(
                availableHours = currentData.availableHours?.copy(
                    dayOfWeek = currentData.availableHours.dayOfWeek?.map { day ->
                        if (day.dayNumber == dayNumber) {
                            val newTimeRange = TimeRange(
                                hourFrom = Hour(startingHour, startingMinute),
                                hourTo = Hour(endingHour, endingMinute)
                            )

                            day.hours?.forEach { existingTime ->
                                val existingFrom = existingTime.hourFrom
                                val existingTo = existingTime.hourTo

                                if (!(newTimeRange.hourTo.hour < existingFrom.hour ||
                                            (newTimeRange.hourTo.hour == existingFrom.hour && newTimeRange.hourTo.minute <= existingFrom.minute) ||
                                            newTimeRange.hourFrom.hour > existingTo.hour ||
                                            (newTimeRange.hourFrom.hour == existingTo.hour && newTimeRange.hourFrom.minute >= existingTo.minute))) {
                                    setError(context.getString(R.string.hours_conflict_error))
                                    return false
                                }
                            }
                            day.copy(
                                hours = (day.hours ?: emptyList()) + newTimeRange
                            )
                        } else {
                            day
                        }
                    }
                )
            )
        }

        return true
    }

    fun updateAvailableHours(formData: AvailableHoursFormData, panelViewModel: PanelViewModel) {
        viewModelScope.launch {
            val response = repository.updateAvailableHours(formData)

            if (response == null) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            } else {
                if (response.status == "success") {
                    Toast.makeText(context, context.getString(R.string.updated_hours_success), Toast.LENGTH_SHORT).show()
                    panelViewModel.checkVerification()
                } else {
                    Toast.makeText(context, context.getString(R.string.error_updating_hours), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setError(message: String?) {
        hoursError.value = message
    }

    fun deleteHour(day: Day, index: Int) {
        day.hours = day.hours?.toMutableList()?.apply {
            if (index in indices) removeAt(index)
        }

        _formData.update { currentData ->
            currentData.copy(
                availableHours = currentData.availableHours?.copy(
                    dayOfWeek = currentData.availableHours.dayOfWeek?.map { d ->
                        if (d.dayNumber == day.dayNumber) {
                            day.copy(
                                hours = day.hours
                            )
                        } else {
                            d
                        }
                    }
                )
            )
        }
    }
}

data class AvailableHoursFormData(
    val availableHours: AvailableHours? = null,
    val scenario: String = "updateAvailableHours"
)