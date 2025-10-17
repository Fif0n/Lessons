package com.example.lessons.ui

import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.example.lessons.MainActivity
import com.example.lessons.ui.teacher.PanelActivity

class TeacherLoginActivity : AbstractLoginActivity("teacher", Color.LightGray, PanelActivity::class.java) {
    override fun redirectToSignup() {
        Intent(this, TeacherSignupActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun redirectToLogin() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }
}