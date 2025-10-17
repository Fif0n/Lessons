package com.example.lessons.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lessons.repositories.LoginRepository
import com.example.lessons.utils.NavigationEvent
import com.example.lessons.viewModels.LoginState
import com.example.lessons.viewModels.LoginViewModel
import androidx.lifecycle.Observer
import com.example.lessons.auth.AuthManager
import com.example.lessons.utils.LoggedUser
import com.example.lessons.ui.student.PanelActivity as StudentPanel
import com.example.lessons.ui.teacher.PanelActivity as TeacherPanel
import kotlinx.coroutines.launch

abstract class AbstractLoginActivity(
    private val role: String,
    private val bgColor: Color,
    private val dashboardView: Class<*>
) : AppCompatActivity() {
    private val viewModel = LoginViewModel(LoginRepository(this))

    protected abstract fun redirectToSignup()
    protected abstract fun redirectToLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authManager = AuthManager(this)

        if (authManager.isLogged()) {
            LoggedUser
            if (authManager.checkAccess("teacher")) {
                Intent(this, TeacherPanel::class.java).also {
                    startActivity(it)
                }
            } else if (authManager.checkAccess("student")) {
                Intent(this, StudentPanel::class.java).also {
                    startActivity(it)
                }
            }
            finish()
        }

        setContent {
            LoginLayout(this)
            viewModel.navigationEvent.observe(this, Observer { event ->
                when (event) {
                    is NavigationEvent.Redirect -> startActivity(event.intent)
                }
            })
        }
    }

    @Composable
    private fun LoginLayout(context: Context) {
        val emailError by viewModel.emailError.collectAsState()
        val passwordError by viewModel.passwordError.collectAsState()
        val loginState by viewModel.loginState.collectAsState()

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = bgColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login - ${role.replaceFirstChar { it.titlecase() }}",
                    fontSize = 36.sp,
                    modifier = Modifier
                        .padding(60.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        isError = emailError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)

                    )
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        isError = passwordError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = { viewModel.login(email, password, role) },
                        shape = RoundedCornerShape(30),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Text(
                        text = "Don't have account yet? Create account",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(bottom = 14.dp)
                            .clickable { redirectToSignup() },
                    )
                    Text(
                        text = "Are you a ${getOppositeRole()}? Login here",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable { redirectToLogin() }
                    )

                    when (loginState) {
                        is LoginState.Idle -> null
                        is LoginState.Loading -> CircularProgressIndicator()
                        is LoginState.Success -> SuccessfulSignupAlert(context)
                        is LoginState.Error -> null
                    }
                }
            }
        }
    }

    @Composable
    private fun SuccessfulSignupAlert(context: Context) {
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            scope.launch {
                viewModel.redirectToDashboard(context, dashboardView)
            }
        }
    }

    private fun getOppositeRole(): String {
        return if (role == "student") "teacher" else "student";
    }

}