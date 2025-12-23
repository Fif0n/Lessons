package com.example.lessons.utils

import android.content.Context
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }

        val languageTag = locale.toLanguageTag() // e.g. en-US

        val request = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", languageTag)
            .build()

        return chain.proceed(request)
    }
}