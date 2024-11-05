package com.vistula.testjetpackcomposeproject.View.ViewModels

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.vistula.testjetpackcomposeproject.View.States.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val TAG = "AuthenticationViewModel"

class AuthenticationViewModel: ViewModel() {

    private var auth = FirebaseAuth.getInstance()

    var authState by mutableStateOf(AuthState())
        private set

    private fun updateLoginStatus(value: Boolean) {
        authState = authState.copy(isLoggedIn = value)
    }

    fun updateEmailTextField(value: String) {
        authState = authState.copy(emailTextField = value)
    }

    fun updatePasswordTextField(value: String) {
        authState = authState.copy(passwordTextField = value)
    }

    init {
        checkLoggedInState()
    }

    fun registerUser() {
        val emailError = if (authState.emailTextField.isEmpty()) "Email cannot be empty"
        else if (!isValidEmail(authState.emailTextField)) "Please enter a valid email"
        else null

        val passwordError = if (authState.passwordTextField.isEmpty()) "Password cannot be empty"
        else null

        authState = authState.copy(
            emailError = emailError,
            passwordError = passwordError
        )

//        if (emailError == null && passwordError == null) {
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    auth.createUserWithEmailAndPassword(authState.emailTextField, authState.passwordTextField).await()
//                    checkLoggedInState()
//                } catch (e: Exception) {
//                    Log.e(TAG, "registerUser: ${e.message}", )
//                }
//            }
//        }
    }

    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            updateLoginStatus(false)
        } else {
            updateLoginStatus(true)
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}