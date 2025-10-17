package com.example.lessons.repositories

import com.example.lessons.auth.TokenManager
import android.content.Context
import android.util.Log
import com.example.lessons.auth.LoginRequest
import com.example.lessons.auth.LoginResponse
import com.example.lessons.modules.backendApi.Api
import com.example.lessons.utils.LoggedUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(val context: Context): Api(context) {
    fun login(data: LoginRequest, onResult: (LoginResponse?) -> Unit) {
        val apiService = provideApiService()

        apiService.login(data).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val tokenManager = TokenManager(context)
                    tokenManager.clearJwt()
                    response.body()?.let {
                        val token = it.token.toString()
                        tokenManager.saveJwt(token)
                        val user = it.data["user"] as Map<*, *>

                        LoggedUser.logIn(
                            user["_id"].toString(),
                            user["name"].toString(),
                            user["surname"].toString(),
                            user["role"].toString()
                        )
                    }

                    onResult(response.body())
                } else {
                    val gson = Gson()
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
                            onResult(errorResponse)
                        } catch (e: Exception) {
                            onResult(null)
                        }
                    } else {
                        onResult(null)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val message = t.message.toString()
                onResult(null)
            }
        })
    }
}