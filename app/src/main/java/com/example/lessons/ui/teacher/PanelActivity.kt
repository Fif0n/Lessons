package com.example.lessons.ui.teacher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.example.lessons.repositories.UserRepository
import com.example.lessons.ui.ProtectedActivity
import com.example.lessons.ui.teacher.navigation.Screen
import com.example.lessons.viewModels.teacher.PanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PanelActivity : ProtectedActivity() {
    override val requiredRole: String = "teacher"

    private lateinit var viewModel: PanelViewModel

    @RequiresApi(35)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = UserRepository(applicationContext)
        viewModel = PanelViewModel(repository)

        setContent {
            Surface {
                Screen(viewModel, this)
            }
        }
    }
}

