package com.example.lessons.ui

import android.content.Intent
import androidx.compose.ui.graphics.Color

class TeacherSignupActivity : AbstractSignupActivity("teacher", Color.LightGray, TeacherLoginActivity::class.java) {
    override fun redirectToSignup() {
        Intent(this, StudentSignupActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun redirectToLogin() {
        Intent(this, TeacherLoginActivity::class.java).also {
            startActivity(it)
        }
    }
}