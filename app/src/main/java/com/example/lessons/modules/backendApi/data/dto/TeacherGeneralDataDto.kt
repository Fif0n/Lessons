package com.example.lessons.modules.backendApi.data.dto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

data class TeacherGeneralDataDto(
    val id: String,
    val email: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val image: String?,
    val ordinalNumber: Int,
    val ratingAvg: Float,
    val ratingCount: Int,
    val description: String,
) {
    fun decodeImage(): Bitmap? {
        if (image == null) return null

        return try {
            val pureBase64 = image.substringAfter("base64,", "")
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}
