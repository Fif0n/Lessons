package com.example.lessons.repositories

import android.content.Context
import com.example.lessons.auth.TokenManager
import com.example.lessons.models.User
import com.example.lessons.modules.backendApi.Api
import com.example.lessons.modules.backendApi.responses.AvailableHoursResponse
import com.example.lessons.modules.backendApi.responses.LessonsSettingsResponse
import com.example.lessons.modules.backendApi.responses.ProfileDataResponse
import com.example.lessons.modules.backendApi.responses.UpdateAvailableHoursResponse
import com.example.lessons.modules.backendApi.responses.UpdateUserResponse
import com.example.lessons.ui.formDatas.BasicFormData
import com.example.lessons.ui.formDatas.UpdatePasswordFormData
import com.example.lessons.utils.LoggedUser
import com.example.lessons.viewModels.teacher.AvailableHoursFormData
import com.example.lessons.viewModels.teacher.LessonsSettingsFormData

class UserRepository(val context: Context) {
    private val apiService = Api(context)

    suspend fun getLoggedUserData(): User? {
        return apiService.getUserData()
    }

    private fun updateJwtToken(token: String) {
        val tokenManager = TokenManager(context)
        tokenManager.saveJwt(token)
    }

    suspend fun updateUser(formData: BasicFormData): UpdateUserResponse? {
        val response = apiService.updateUser(formData)
        if (response != null && response.status == "success" && response.data.user != null && response.token != null) {
            updateJwtToken(response.token)
            LoggedUser.updateNames(
                response.data.user.name,
                response.data.user.surname
            )
        }

        return response
    }

    suspend fun updatePassword(formData: UpdatePasswordFormData): UpdateUserResponse? {
        val response = apiService.updateUserPassword(formData)

        if (response != null && response.status == "success" && response.data.user != null && response.token != null) {
            updateJwtToken(response.token)
        }

        return response
    }

    suspend fun getLessonsSettings(): LessonsSettingsResponse {
        return apiService.getLessonsSettings()
    }

    suspend fun updateUserLessonsSetting(formData: LessonsSettingsFormData): UpdateUserResponse? {
        return apiService.updateUserLessonsSetting(formData)
    }

    suspend fun getAvailableHours(): AvailableHoursResponse? {
        return apiService.getAvailableHours()
    }

    suspend fun updateAvailableHours(formData: AvailableHoursFormData): UpdateAvailableHoursResponse? {
        return apiService.updateAvailableHours(formData)
    }

    suspend fun getProfileData(): ProfileDataResponse? {
        return apiService.getProfileData()
    }
}