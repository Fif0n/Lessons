package com.example.lessons.auth

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "jwt_prefs"
        private const val JWT_KEY = "jwt_token"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveJwt(token: String) {
        sharedPreferences.edit().putString(JWT_KEY, token).apply()
    }

    fun getJwt(): String? {
        return sharedPreferences.getString(JWT_KEY, null)
    }

    fun clearJwt() {
        sharedPreferences.edit().remove(JWT_KEY).apply()
    }
}
