package com.example.lessons.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

data class Location(
    val coordinates: List<Double>?,
    val address: String?
)

data class User(
    val _id: String,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: String,
    val subject: List<String>?,
    val schoolLevel: List<String>?,
    val lessonPlace: List<String>?,
    val location: Location?,
    val lessonsPlatform: String?,
    val lessonMoneyRate: Int?,
    val lessonLength: Int?,
    val phoneNumber: String?,
    val profileImage: String?,
    val yourselfDescription: String?,
    val verified: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val availableHours: AvailableHours?,
    val avatar: String,
    val subjectsTranslated: List<String>?,
    val schoolLevelsTranslated: List<String>?,
    val lessonPlacesTranslated: List<String>?,

) {
    fun decodeImage(): Bitmap? {
        return try {
            val input = avatar ?: ""
            val pureBase64 = if (input.isBlank()) "" else input.substringAfter("base64,", "")
            if (pureBase64.isBlank()) return null
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun subjectsFormatted(): String {
        if (subjectsTranslated == null) return "-"
        return subjectsTranslated.joinToString(", ")
    }

    fun schoolLevelsFormatted(): String {
        if (schoolLevelsTranslated == null) return "-"
        return schoolLevelsTranslated.joinToString(", ")
    }

    fun lessonsPlacesFormatted(): String {
        if (lessonPlacesTranslated == null) return "-"
        return lessonPlacesTranslated.joinToString(", ")
    }

    fun getFullName(): String {
        return "$name $surname"
    }
}