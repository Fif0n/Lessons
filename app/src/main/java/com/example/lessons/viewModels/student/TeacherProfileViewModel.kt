package com.example.lessons.viewModels.student

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.models.Hour
import com.example.lessons.models.LessonRequestHours
import com.example.lessons.models.Rating
import com.example.lessons.models.TimeRange
import com.example.lessons.models.User
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.requests.EditRatingRequest
import com.example.lessons.modules.backendApi.requests.LessonRequestRequest
import com.example.lessons.modules.backendApi.requests.PostRatingRequest
import com.example.lessons.modules.backendApi.responses.LessonRequestResponse
import com.example.lessons.modules.backendApi.responses.RatingResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TeacherProfileViewModel(
    private val apiService: ApiService,
    private val id: String?,
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    private val _rating = MutableStateFlow<Rating?>(null)
    val rating: StateFlow<Rating?> = _rating

    private val _availableHours = MutableStateFlow<List<String>?>(null)
    val availableHours: StateFlow<List<String>?> = _availableHours

    private val _startingHour = MutableStateFlow<String?>(null)
    val startingHour: StateFlow<String?> = _startingHour

    private val _subject = MutableStateFlow<String>("")
    val subject: StateFlow<String> = _subject

    private val _schoolLevel = MutableStateFlow<String>("")
    val schoolLevel: StateFlow<String> = _schoolLevel

    private val _lessonPlace = MutableStateFlow<String>("")
    val lessonPlace: StateFlow<String> = _lessonPlace

    private val _comment = MutableStateFlow<String>("")
    val comment: StateFlow<String> = _comment

    var subjectError = MutableStateFlow<String?>(null)
        private set
    var schoolLevelError = MutableStateFlow<String?>(null)
        private set
    var lessonPlaceError = MutableStateFlow<String?>(null)
        private set
    var commentError = MutableStateFlow<String?>(null)
        private set

    private val _subjectEnums = MutableStateFlow<Map<String, String>>(emptyMap())
    val subjectsEnum: StateFlow<Map<String, String>> = _subjectEnums

    private val _schoolLevelEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val schoolLevelEnum: StateFlow<Map<String, String>> = _schoolLevelEnum

    private val _lessonPlaceEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val lessonPlaceEnum: StateFlow<Map<String, String>> = _lessonPlaceEnum

    var canSendRating: Boolean = false
        private set

    init {
        viewModelScope.launch {
            if (id != null) {
                try {
                    val response = apiService.getTeacherData(id)

                    if (response.status == "success") {
                        _userData.value = response.data.user
                        _rating.value = response.data.rate
                        if (response.data.canLeaveComment != null) {
                            canSendRating = response.data.canLeaveComment
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setAvailableHours(date: String, dayName: String) {
        viewModelScope.launch {
            val availableRanges = getAvailableHours(dayName)
            val takenSlots = getTakenHours(date)

            if (_userData.value?.lessonLength != null && availableRanges != null) {
                _availableHours.value = generateAvailableSlots(
                    availableRanges,
                    takenSlots,
                    _userData.value?.lessonLength!!,
                    date
                )
            } else {
                _availableHours.value = null
            }


            setEnums()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateAvailableSlots(
        availableTimeRanges: List<Pair<String, String>>,
        takenHours: List<Pair<String, String>>?,
        lessonDuration: Int,
        dateString: String
    ): List<String> {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun parseTime(time: String) = LocalTime.parse(time, timeFormatter)

        val availableSlots = mutableListOf<String>()
        val now = LocalTime.now()
        val today = LocalDate.now()
        val inputDate = LocalDate.parse(dateString, dateFormatter)

        for ((start, end) in availableTimeRanges) {
            var currentTime = parseTime(start)
            val endTime = parseTime(end)

            while (currentTime.plusMinutes(lessonDuration.toLong()) <= endTime) {
                val nextTime = currentTime.plusMinutes(lessonDuration.toLong())
                val slot = currentTime.format(timeFormatter)

                if (inputDate == today && currentTime < now) {
                    currentTime = nextTime
                    continue
                }

                val isTaken = takenHours?.any { (takenStart, takenEnd) ->
                    val takenStartTime = parseTime(takenStart)
                    val takenEndTime = parseTime(takenEnd)

                    !(nextTime <= takenStartTime || currentTime >= takenEndTime)
                } ?: false

                if (!isTaken) {
                    availableSlots.add(slot)
                }

                currentTime = nextTime
            }
        }

        return availableSlots
    }

    private suspend fun getTakenHours(date: String): List<Pair<String, String>>? {
        val response = _userData.value?.let { apiService.getTakenHours(it._id, date) }

        return response?.data?.let { parseLessonRequestHours(it) }
    }

    private fun parseTimeRanges(timeRanges: List<TimeRange>): List<Pair<String, String>> {
        return timeRanges.map { range ->
            range.hourFrom.toFormattedString() to range.hourTo.toFormattedString()
        }
    }

    private fun parseLessonRequestHours(timeRanges: List<LessonRequestHours>): List<Pair<String, String>> {
        return timeRanges.map { range ->
            range.startingHour.toFormattedString() to range.endingHour.toFormattedString()
        }
    }

    private fun getAvailableHours(dayName: String): List<Pair<String, String>>? {
        val days = userData.value?.availableHours?.dayOfWeek

        val day = days?.find { it.dayName == dayName }

        if (day?.hours == null) {
            return null
        }

        return parseTimeRanges(day.hours!!)
    }

    fun setStartingHour(hour: String) {
        _startingHour.value = hour
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendLessonRequest(comment: String?, date: String, startingHour: String, context: Context, onSuccess: () -> Unit) {
        _userData.value?.lessonLength?.let {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            val startingHourFormated = LocalTime.parse(startingHour, formatter)

            val time = LocalTime.parse(startingHour, formatter)
            val endingHour = time.plusMinutes(it.toLong())

            val hours = LessonRequestHours(
                startingHour = Hour(
                    startingHourFormated.hour,
                    startingHourFormated.minute
                ),
                endingHour = Hour(
                    endingHour.hour,
                    endingHour.minute
                )
            )

            clearErrors()

            viewModelScope.launch {
                val response = try {
                    apiService.sendLessonRequest(
                        _userData.value!!._id,
                        LessonRequestRequest(
                            date,
                            hours,
                            _subject.value,
                            _schoolLevel.value,
                            _lessonPlace.value,
                            comment,
                        )
                    )
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Gson().fromJson(errorBody, LessonRequestResponse::class.java)
                } catch (e: Exception) {
                    null
                }

                if (response != null) {
                    if (response.status == "success") {
                        Toast.makeText(context, "Request send successfully", Toast.LENGTH_SHORT).show()
                        clearFields()
                        onSuccess()
                        _availableHours.value = null
                    } else {
                        if (response.data?.data != null) {
                            setErrors(response.data.data)
                        }
                        Toast.makeText(context, "Something went wrong. Try again later", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Service is currently unavailable. Try again later", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateFormField(field: String, value: String) {
        if (field == "subject") _subject.value = value
        if (field == "schoolLevel") _schoolLevel.value = value
        if (field == "lessonPlace") _lessonPlace.value = value
        if (field == "comment") _comment.value = value
    }

    private fun clearErrors() {
        subjectError.value = null
        schoolLevelError.value = null
        lessonPlaceError.value = null
        commentError.value = null
    }

    private fun setErrors(data: Map<String, String>) {
        data.forEach { i ->
            val value = i.value
            when (i.key) {
                "subject" -> subjectError.value = value
                "schoolLevel" -> schoolLevelError.value = value
                "lessonPlace" -> lessonPlaceError.value = value
                "comment" -> commentError.value = value
            }
        }
    }

    private suspend fun setEnums() {
        val response = apiService.getLessonsSettings()

        fun filter(list: List<String>?, map: Map<String, String>): Map<String, String>? {
            if (list != null) {
                return map.filterKeys { it in list }
            }

            return null
        }

        val subjectsFiltered = filter(_userData.value?.subject, response.data.enums.subjects)
        val schoolLevelsFiltered = filter(_userData.value?.schoolLevel, response.data.enums.schoolLevels)
        val lessonPlacesFiltered = filter(_userData.value?.lessonPlace, response.data.enums.lessonPlaces)

        if (subjectsFiltered != null) {
            _subjectEnums.value = subjectsFiltered
        }

        if (schoolLevelsFiltered != null) {
            _schoolLevelEnum.value = schoolLevelsFiltered
        }

        if (lessonPlacesFiltered != null) {
            _lessonPlaceEnum.value = lessonPlacesFiltered
        }
    }

    fun postRating(teacherId: String, rating: Float, text: String?, context: Context) {
        viewModelScope.launch {
            val body = PostRatingRequest(
                teacherId,
                rating,
                text
            )

            try {
                val response = apiService.postRating(body)

                if (response.status == "success") {
                    Toast.makeText(context, "Rating send successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(errorBody, RatingResponse::class.java)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun editRating(id: String, rating: Float, text: String?, context: Context) {
        viewModelScope.launch {
            val body = EditRatingRequest(
                rating,
                text
            )

            try {
                val response = apiService.updateRating(id, body)

                if (response.status == "success") {
                    Toast.makeText(context, "Rating has been updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(errorBody, RatingResponse::class.java)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                Log.d("ErrorSend", e.toString())
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun clearFields() {
        _subject.value = ""
        _schoolLevel.value = ""
        _lessonPlace.value = ""
        _comment.value = ""
    }

}