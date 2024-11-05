package com.vistula.testjetpackcomposeproject.View.States

data class AuthState(
    val emailTextField: String = "",
    val passwordTextField: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoggedIn: Boolean = false
)