package com.example.lessons.modules.backendApi

import com.example.lessons.auth.TokenManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.lessons.auth.AuthInterceptor
import com.example.lessons.models.User
import com.example.lessons.modules.backendApi.responses.AvailableHoursResponse
import com.example.lessons.modules.backendApi.responses.LessonsSettingsResponse
import com.example.lessons.modules.backendApi.responses.ProfileDataResponse
import com.example.lessons.modules.backendApi.responses.UpdateAvailableHoursResponse
import com.example.lessons.modules.backendApi.responses.UpdateUserResponse
import com.example.lessons.ui.formDatas.BasicFormData
import com.example.lessons.ui.formDatas.UpdatePasswordFormData
import com.example.lessons.viewModels.teacher.AvailableHoursFormData
import com.example.lessons.viewModels.teacher.LessonsSettingsFormData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

open class Api(private val context: Context) {
    private val baseUrl: String = com.example.lessons.BuildConfig.BASE_URL

    protected fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun provideApiServiceWithBearer(): ApiService {
        val tokenManager = TokenManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getUserData(): User? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserData()
                response.data.user
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateUser(formData: BasicFormData): UpdateUserResponse? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                apiService.updateUser(formData)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, UpdateUserResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateUserPassword(formData: UpdatePasswordFormData): UpdateUserResponse? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                apiService.updateUserPassword(formData)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, UpdateUserResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getLessonsSettings(): LessonsSettingsResponse {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            apiService.getLessonsSettings()
        }
    }

    suspend fun updateUserLessonsSetting(formData: LessonsSettingsFormData): UpdateUserResponse? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                apiService.updateUserLessonsSetting(formData)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, UpdateUserResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getAvailableHours(): AvailableHoursResponse? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                apiService.getAvailableHours()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, AvailableHoursResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateAvailableHours(formData: AvailableHoursFormData): UpdateAvailableHoursResponse? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                apiService.updateAvailableHours(formData)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, UpdateAvailableHoursResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getProfileData(): ProfileDataResponse? {
        val apiService = provideApiServiceWithBearer()

        return withContext(Dispatchers.IO) {
            try {
                apiService.getProfileData()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, ProfileDataResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getUserAvatar(): Uri? {
        val response = provideApiServiceWithBearer().getAvatar()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let { responseBody ->
                val inputStream = responseBody.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val imageFile = File(context.cacheDir, "image.jpg")
                FileOutputStream(imageFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                }

                return Uri.fromFile(imageFile)
            }
        }
        return null
    }
}