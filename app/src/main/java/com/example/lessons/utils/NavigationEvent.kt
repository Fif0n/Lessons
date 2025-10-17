package com.example.lessons.utils

import android.content.Intent

sealed class NavigationEvent {
    data class Redirect(val intent: Intent) : NavigationEvent()
}