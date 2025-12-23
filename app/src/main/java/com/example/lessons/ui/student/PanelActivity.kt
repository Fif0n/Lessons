package com.example.lessons.ui.student

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.example.lessons.ui.ProtectedActivity
import com.example.lessons.ui.student.navigation.Screen
import com.example.lessons.viewModels.student.PanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PanelActivity: ProtectedActivity() {
    override val requiredRole: String = "student"

    @RequiresApi(35)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = PanelViewModel(this)

        setContent {
            Surface {
                Screen(viewModel, this)
            }
        }
    }
}

