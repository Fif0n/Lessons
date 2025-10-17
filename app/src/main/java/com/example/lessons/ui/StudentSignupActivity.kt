package com.example.lessons.ui

import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.example.lessons.MainActivity

class StudentSignupActivity : AbstractSignupActivity("student", Color.White, MainActivity::class.java) {
    override fun redirectToLogin() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun redirectToSignup() {
        Intent(this, TeacherSignupActivity::class.java).also {
            startActivity(it)
        }
    }
}