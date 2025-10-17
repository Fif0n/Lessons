package com.example.lessons.ui

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lessons.MainActivity
import com.example.lessons.auth.AuthManager

abstract class ProtectedActivity: AppCompatActivity() {
    abstract val requiredRole: String?

    override fun onStart() {
        super.onStart()

        val authManager = AuthManager(this)

        if (!authManager.isLogged()) {
            redirectToLogin()
        } else if (requiredRole != null && !authManager.checkAccess(requiredRole)) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show()
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}