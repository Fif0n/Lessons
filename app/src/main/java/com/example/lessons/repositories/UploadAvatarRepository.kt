package com.example.lessons.repositories

import android.content.Context
import android.net.Uri
import com.example.lessons.auth.LoginResponse
import com.example.lessons.modules.backendApi.Api
import com.example.lessons.modules.backendApi.responses.UpdateUserResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadAvatarRepository(val context: Context): Api(context) {

    private fun getFileFromUri(uri: Uri, context: Context): File? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output -> inputStream.copyTo(output) }
        return tempFile
    }

    fun uploadAvatar(uri: Uri, onResult: (UpdateUserResponse?) -> Unit) {
        val file = getFileFromUri(uri, context) ?: return

        val requestFile = file.asRequestBody("image/".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val apiService = provideApiServiceWithBearer()

        apiService.uploadAvatar(body).enqueue(object : Callback<UpdateUserResponse> {
            override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                }

            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun downloadImage() {

    }
}