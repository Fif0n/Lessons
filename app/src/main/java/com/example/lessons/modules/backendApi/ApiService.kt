package com.example.lessons.modules.backendApi

import com.example.lessons.auth.LoginRequest
import com.example.lessons.auth.LoginResponse
import com.example.lessons.auth.SignupRequest
import com.example.lessons.auth.SignupResponse
import com.example.lessons.models.Rating
import com.example.lessons.modules.backendApi.data.dto.ConversationDto
import com.example.lessons.modules.backendApi.data.dto.CurrentWeekLessonsDto
import com.example.lessons.modules.backendApi.data.dto.LessonRequestDto
import com.example.lessons.modules.backendApi.data.dto.TeacherGeneralDataDto
import com.example.lessons.modules.backendApi.requests.EditRatingRequest
import com.example.lessons.modules.backendApi.requests.LessonRequestRequest
import com.example.lessons.modules.backendApi.requests.LessonRequestStatus
import com.example.lessons.modules.backendApi.requests.PostRatingRequest
import com.example.lessons.modules.backendApi.requests.SendMessageRequest
import com.example.lessons.modules.backendApi.requests.SetLessonRequestLink
import com.example.lessons.modules.backendApi.responses.AvailableHoursResponse
import com.example.lessons.modules.backendApi.responses.ConversationResponse
import com.example.lessons.modules.backendApi.responses.EstimatedIncomeResponse
import com.example.lessons.modules.backendApi.responses.LessonRequestEnumsResponse
import com.example.lessons.modules.backendApi.responses.LessonRequestResponse
import com.example.lessons.modules.backendApi.responses.LessonsHistoryEnums
import com.example.lessons.modules.backendApi.responses.LessonsSettingsResponse
import com.example.lessons.modules.backendApi.responses.PostLessonRequestStatusResponse
import com.example.lessons.modules.backendApi.responses.ProfileDataResponse
import com.example.lessons.modules.backendApi.responses.RatingResponse
import com.example.lessons.modules.backendApi.responses.SendMessageResponse
import com.example.lessons.modules.backendApi.responses.TakenHoursResponse
import com.example.lessons.modules.backendApi.responses.UpdateAvailableHoursResponse
import com.example.lessons.modules.backendApi.responses.UpdateUserResponse
import com.example.lessons.modules.backendApi.responses.UserApiResponse
import com.example.lessons.ui.formDatas.BasicFormData
import com.example.lessons.ui.formDatas.UpdatePasswordFormData
import com.example.lessons.viewModels.teacher.AvailableHoursFormData
import com.example.lessons.viewModels.teacher.LessonsSettingsFormData
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("signup")
    fun signup(@Body body: SignupRequest): Call<SignupResponse>

    @POST("login")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    @GET("user/user-data")
    suspend fun getUserData(): UserApiResponse

    @PUT("user/user-data")
    suspend fun updateUser(@Body body: BasicFormData): UpdateUserResponse

    @PUT("user/update-password")
    suspend fun updateUserPassword(@Body body: UpdatePasswordFormData): UpdateUserResponse

    @GET("user/lesson-settings")
    suspend fun getLessonsSettings(): LessonsSettingsResponse

    @PUT("user/lesson-settings")
    suspend fun updateUserLessonsSetting(@Body body: LessonsSettingsFormData): UpdateUserResponse

    @GET("user/available-hours")
    suspend fun getAvailableHours(): AvailableHoursResponse

    @PUT("user/available-hours")
    suspend fun updateAvailableHours(@Body body: AvailableHoursFormData): UpdateAvailableHoursResponse

    @GET("user/profile-data")
    suspend fun getProfileData(): ProfileDataResponse

    @Multipart
    @POST("user/upload-avatar")
    fun uploadAvatar(@Part image: MultipartBody.Part): Call<UpdateUserResponse>

    @GET("user/get-avatar")
    suspend fun getAvatar(): Response<ResponseBody>

    @GET("user/estimated-in come")
    suspend fun getEstimatedIncome(): EstimatedIncomeResponse

    @GET("teacher/teachers")
    suspend fun getTeachersGeneralData(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
        @Query("subject") subject: List<String>?,
        @Query("school_level") schoolLevel: List<String>?,
        @Query("lesson_place") lessonPlace: List<String>?,
        @Query("money_rate") moneyRate: String,
        @Query("min_lesson_length") minLessonLength: String,
        @Query("max_lesson_length") maxLessonLength: String,
    ): List<TeacherGeneralDataDto>

    @GET("teacher/teacher/{id}")
    suspend fun getTeacherData(@Path("id") id: String): UpdateUserResponse

    @GET("teacher/available-lesson-requests-hours/{id}/{date}")
    suspend fun getTakenHours(@Path("id") id: String, @Path("date") date: String): TakenHoursResponse

    @POST("teacher/lesson-request/{id}")
    suspend fun sendLessonRequest(@Path("id") id: String, @Body body: LessonRequestRequest): LessonRequestResponse

    @GET("teacher/ratings/{id}")
    suspend fun getTeacherRatings(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
    ): List<Rating>

    @GET("user/opinions-about-me")
    suspend fun getOpinionsAboutMe(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
    ): List<Rating>

    @GET("lesson-request/lesson-requests")
    suspend fun getLessonRequestsData(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
        @Query("status") status: String,
        @Query("id") id: String
    ): List<LessonRequestDto>

    @GET("lesson-request/lesson-requests-enums")
    suspend fun getLessonRequestEnums(): LessonRequestEnumsResponse

    @GET("lesson-request/lessons-history-enums")
    suspend fun getLessonsHistoryEnums(): LessonsHistoryEnums

    @GET("lesson-request/lesson-request/{id}")
    suspend fun getLessonRequest(@Path("id") id: String): LessonRequestResponse

    @POST("lesson-request/lesson-request/{id}")
    suspend fun postLessonRequestStatus(@Path("id") id: String, @Body body: LessonRequestStatus): PostLessonRequestStatusResponse

    @PUT("lesson-request/set-lesson-link/{id}")
    suspend fun setLessonLink(@Path("id") id: String, @Body body: SetLessonRequestLink): LessonRequestResponse

    @GET("conversation/conversation/{id}")
    suspend fun getConversation(@Path("id") id: String): ConversationResponse

    @GET("conversation/conversations")
    suspend fun getConversations(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
        @Query("id") id: String
    ): List<ConversationDto>

    @POST("conversation/send-message/{id}")
    suspend fun sendMessage(@Path("id") id: String, @Body body: SendMessageRequest): SendMessageResponse

    @GET("lesson-request/incoming-lessons/{year}/{week}")
    suspend fun getIncomingLessons(@Path("year") year: Int, @Path("week") week: Int): List<CurrentWeekLessonsDto>?

    @GET("lesson-request/history")
    suspend fun getLessonsHistory(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
        @Query("id") id: String,
        @Query("name") name: String,
        @Query("subject") subject: String,
    ): List<LessonRequestDto>

    @POST("rating/rate")
    suspend fun postRating(@Body body: PostRatingRequest): RatingResponse

    @PUT("rating/rate/{id}")
    suspend fun updateRating(@Path("id") id: String, @Body body: EditRatingRequest): RatingResponse
}