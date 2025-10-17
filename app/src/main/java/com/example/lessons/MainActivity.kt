package com.example.lessons

import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.example.lessons.ui.AbstractLoginActivity
import com.example.lessons.ui.StudentSignupActivity
import com.example.lessons.ui.TeacherLoginActivity
import com.example.lessons.ui.student.PanelActivity

class MainActivity : AbstractLoginActivity("student", Color.White, PanelActivity::class.java) {
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