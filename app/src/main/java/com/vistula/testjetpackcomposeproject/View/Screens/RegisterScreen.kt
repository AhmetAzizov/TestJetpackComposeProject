package com.vistula.testjetpackcomposeproject.View.Screens

import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.Logger
import com.vistula.testjetpackcomposeproject.R
import com.vistula.testjetpackcomposeproject.View.States.AuthState
import com.vistula.testjetpackcomposeproject.View.ViewModels.AuthenticationViewModel

private const val TAG = "RegisterScreen"

@Composable
fun RegisterScreen(
    viewModel: AuthenticationViewModel,
    navController: NavHostController
) {
//    LaunchedEffect(viewModel.authState.isLoggedIn) {
//        if (viewModel.authState.isLoggedIn) {
//            navController.navigate("main")
//        }
//    }

    RegisterScreenContent(
        navController = navController,
        state = viewModel.authState,
        updateUsernameTextField = { viewModel.updateUsernameEmailTextField(it) },
        updateEmailTextField = { viewModel.updateEmailTextField(it) },
        updatePasswordTextField = { viewModel.updatePasswordTextField(it) },
        updateImageUri = { viewModel.updateImageUri(it) },
        registerUser = { onSuccess, onFailure ->
            viewModel.registerUser(onSuccess, onFailure) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenContent(
    navController: NavHostController,
    state: AuthState,
    updateUsernameTextField: (String) -> Unit,
    updateEmailTextField: (String) -> Unit,
    updatePasswordTextField: (String) -> Unit,
    updateImageUri: (Uri) -> Unit,
    registerUser: (() -> Unit, (Exception) -> Unit) -> Unit
) {
    val context = LocalContext.current

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

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { updateImageUri(uri) }
        }

        Card(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(100.dp),
            border = BorderStroke(2.dp, Color.White)
        ) {
            state.imageUri?.let { uri ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = ImageRequest.Builder(context)
                        .data(uri)
                        .crossfade(400).
                        build(),
                    contentDescription = "user image",
                    contentScale = ContentScale.Crop
                )
            }.let {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (state.imageError.isNullOrEmpty()) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            painter = painterResource(R.drawable.image_search),
                            contentDescription = "photo icon"
                        )
                    }
                    else {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(4.dp),
                            color = MaterialTheme.colorScheme.error,
                            text = state.imageError
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp),
            placeholder = { Text("Username") },
            value = state.usernameTextField,
            isError = !state.usernameError.isNullOrEmpty(),
            supportingText = {
                state.usernameError?.let {
                    Text(state.usernameError)
                }
            },
            singleLine = true,
            onValueChange = {value ->
                updateUsernameTextField(value)
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            placeholder = { Text("Email") },
            value = state.emailTextField,
            isError = !state.emailError.isNullOrEmpty(),
            supportingText = {
                state.emailError?.let {
                    Text(state.emailError)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                registerUser(
                    {
                        Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                    },
                    { exception ->
                        Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                    }
                )
            },
            enabled = !state.registerLoading
        ) {
            Text(
                modifier = Modifier,
                fontSize = 20.sp,
                text = "Register"
            )
            if (state.registerLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(28.dp),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreenContent(
        navController = rememberNavController(),
        state = AuthState(),
        updateUsernameTextField = {},
        updateEmailTextField = {},
        updatePasswordTextField = {},
        updateImageUri = {},
        registerUser = {onSuccess, onFailure -> }
    )
}

