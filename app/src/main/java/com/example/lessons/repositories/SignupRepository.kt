package com.example.lessons.repositories

import android.content.Context
import com.example.lessons.auth.SignupRequest
import com.example.lessons.auth.SignupResponse
import com.example.lessons.modules.backendApi.Api
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupRepository(context: Context): Api(context) {
    fun signUp(data: SignupRequest, onResult: (SignupResponse?) -> Unit) {
        val apiService = provideApiService()

        apiService.signup(data).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    val gson = Gson()
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = gson.fromJson(errorBody, SignupResponse::class.java)
                            onResult(errorResponse)
                        } catch (e: Exception) {
                            onResult(null)
                        }
                    } else {
                        onResult(null)
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                val message = t.message.toString()
                onResult(null)
            }
        })
    }
}