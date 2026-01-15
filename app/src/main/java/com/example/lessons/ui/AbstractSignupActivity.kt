package com.example.lessons.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.lessons.utils.LocaleUtil
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.example.lessons.repositories.SignupRepository
import com.example.lessons.utils.NavigationEvent
import com.example.lessons.viewModels.SignupState
import com.example.lessons.viewModels.SignupViewModel
import kotlinx.coroutines.launch

abstract class AbstractSignupActivity(
    private val role: String,
    private val bgColor: Color,
    private val loginView: Class<*>
)  : AppCompatActivity() {
    private val viewModel = SignupViewModel(SignupRepository(this))

    protected abstract fun redirectToSignup()
    protected abstract fun redirectToLogin()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.wrapContextWithSystemLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SignupLayout(this)
            viewModel.navigationEvent.observe(this, Observer { event ->
                when (event) {
                    is NavigationEvent.Redirect -> startActivity(event.intent)
                }
            })
        }
    }

    @Composable
    private fun SignupLayout(context: Context) {
        val nameError by viewModel.nameError.collectAsState()
        val surnameError by viewModel.surnameError.collectAsState()
        val emailError by viewModel.emailError.collectAsState()
        val passwordError by viewModel.passwordError.collectAsState()
        val passwordConfirmError by viewModel.passwordConfirmError.collectAsState()
        val signupState by viewModel.signupState.collectAsState()

        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordConfirm by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = bgColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = headerTranslation(role),
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(50.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(com.example.lessons.R.string.name_label)) },
                        isError = nameError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(com.example.lessons.R.string.name_label)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Left
                        )
                    }
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text(stringResource(com.example.lessons.R.string.surname_label)) },
                        isError = surnameError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(com.example.lessons.R.string.surname_label),
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                    if (surnameError != null) {
                        Text(
                            text = surnameError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Left
                        )
                    }
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(com.example.lessons.R.string.email_hint)) },
                        isError = emailError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = stringResource(com.example.lessons.R.string.email_hint)
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
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Left
                        )
                    }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(com.example.lessons.R.string.password_hint)) },
                        isError = passwordError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(com.example.lessons.R.string.password_hint)
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
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Left
                        )
                    }
                    OutlinedTextField(
                        value = passwordConfirm,
                        onValueChange = { passwordConfirm = it },
                        label = { Text(stringResource(com.example.lessons.R.string.password_confirm_label)) },
                        isError = passwordConfirmError != null,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(com.example.lessons.R.string.password_confirm_label)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    if (passwordConfirmError != null) {
                        Text(
                            text = passwordConfirmError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Left
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = { viewModel.signup(name, surname, email, password, passwordConfirm, role) },
                        shape = RoundedCornerShape(30),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(com.example.lessons.R.string.signup),
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Text(
                        text = stringResource(com.example.lessons.R.string.login_redirect),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(bottom = 14.dp)
                            .clickable { redirectToLogin() },
                    )
                    Text(
                        text = stringResource(com.example.lessons.R.string.opposite_role_signup, getOppositeRole()),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable { redirectToSignup() }
                    )

                    when (signupState) {
                        is SignupState.Idle -> null
                        is SignupState.Loading -> CircularProgressIndicator()
                        is SignupState.Success -> SuccessfulSignupAlert(context)
                        is SignupState.Error -> null
                    }
                }
            }
        }
    }

    @Composable
    private fun SuccessfulSignupAlert(context: Context) {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            scope.launch {
                val msg = context.getString(com.example.lessons.R.string.successfully_signed_up)
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )

                viewModel.redirectToLogin(context, loginView)
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }

    @Composable
    private fun getOppositeRole(): String {
        return if (role == "student") stringResource(com.example.lessons.R.string.auth_role_name_teacher) else stringResource(com.example.lessons.R.string.auth_role_name_student);
    }

    @Composable
    private fun headerTranslation(role: String): String {
        if (role === "student") return stringResource(com.example.lessons.R.string.signup_header_student)
        if (role === "teacher") return stringResource(com.example.lessons.R.string.signup_header_teacher)

        return ""
    }
}