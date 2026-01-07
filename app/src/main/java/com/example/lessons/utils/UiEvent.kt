package com.example.lessons.utils

sealed class UiEvent {
    data class ShowMessage(val messageResId: Int) : UiEvent()
    data class ShowText(val text: String) : UiEvent()
}