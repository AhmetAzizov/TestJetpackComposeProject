package com.vistula.testjetpackcomposeproject.View.States

import android.net.Uri

data class AuthState(
    val usernameTextField: String = "",
    val emailTextField: String = "",
    val passwordTextField: String = "",
    val imageUri: Uri? = null,
    val imageError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoggedIn: Boolean = false,
    val registerLoading: Boolean = false
)