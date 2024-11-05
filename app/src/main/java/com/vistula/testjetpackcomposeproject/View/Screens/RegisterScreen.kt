package com.vistula.testjetpackcomposeproject.View.Screens

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vistula.testjetpackcomposeproject.View.States.AuthState
import com.vistula.testjetpackcomposeproject.View.ViewModels.AuthenticationViewModel

private const val TAG = "RegisterScreen"

@Composable
fun RegisterScreen(
    viewModel: AuthenticationViewModel,
    navController: NavHostController
) {
    LaunchedEffect(viewModel.authState.isLoggedIn) {
        if (viewModel.authState.isLoggedIn) {
            navController.navigate("main")
        }
    }

    RegisterScreenContent(
        navController = navController,
        state = viewModel.authState,
        updateEmailTextField = { viewModel.updateEmailTextField(it) },
        updatePasswordTextField = { viewModel.updatePasswordTextField(it) },
        registerUser = { viewModel.registerUser() }
    )
}

@Composable
fun RegisterScreenContent(
    navController: NavHostController,
    state: AuthState,
    updateEmailTextField: (String) -> Unit,
    updatePasswordTextField: (String) -> Unit,
    registerUser: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    navController.navigate("main")
                },
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back Button"
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp),
            placeholder = { Text("Email") },
            value = state.emailTextField,
            isError = !state.emailError.isNullOrEmpty(),
            supportingText = {
                state.emailError?.let {
                    Text(state.emailError)
                }
            },
            singleLine = true,
            onValueChange = {value ->
                updateEmailTextField(value)
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            placeholder = { Text("Password") },
            singleLine = true,
            isError = !state.passwordError.isNullOrEmpty(),
            supportingText = {
                state.passwordError?.let {
                    Text(state.passwordError)
                }
            },
            value = state.passwordTextField,
            onValueChange = {value ->
                updatePasswordTextField(value)
            }
        )

        Button(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            onClick = {
                registerUser()
            }
        ) {
            Text(text = "Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreenContent(
        navController = rememberNavController(),
        state = AuthState(),
        updateEmailTextField = {},
        updatePasswordTextField = {},
        registerUser = {}
    )
}

