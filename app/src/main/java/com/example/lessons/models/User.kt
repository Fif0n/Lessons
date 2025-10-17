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
    val avatar: String
) {
    fun decodeImage(): Bitmap? {
        return try {
            val pureBase64 = avatar.substringAfter("base64,", "")
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    fun subjectsFormatted(): String {
        if (subject == null) return "-"
        return subject.joinToString(", ")
    }

    fun schoolLevelsFormatted(): String {
        if (schoolLevel == null) return "-"
        return schoolLevel.joinToString(", ")
    }

    fun lessonsPlacesFormatted(): String {
        if (lessonPlace == null) return "-"
        return lessonPlace.joinToString(", ")
    }

    fun getFullName(): String {
        return "$name $surname"
    }
}