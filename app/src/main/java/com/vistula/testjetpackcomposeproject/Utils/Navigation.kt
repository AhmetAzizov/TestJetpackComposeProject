package com.vistula.testjetpackcomposeproject.Utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.vistula.testjetpackcomposeproject.View.Screens.AddScreen
import com.vistula.testjetpackcomposeproject.View.Screens.MainScreen
import com.vistula.testjetpackcomposeproject.View.Screens.RegisterScreen
import com.vistula.testjetpackcomposeproject.View.ViewModels.AuthenticationViewModel
import com.vistula.testjetpackcomposeproject.View.ViewModels.ItemViewModel

@Composable
fun Navigation(
) {
    val navController = rememberNavController()

    val startDestination = if (checkLoggedInState()) "main" else "auth"

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            startDestination = Screen.AddScreen.route,
            route = "main"
        ) {
            composable(route = Screen.MainScreen.route) {backStackEntry ->
                val viewModel: ItemViewModel = viewModel(backStackEntry)
                MainScreen(viewModel)
            }

            composable(route = Screen.AddScreen.route) {backStackEntry ->
                val viewModel: ItemViewModel = viewModel(backStackEntry)
                AddScreen(viewModel, navController)
            }
        }

        navigation(
            startDestination = Screen.LoginScreen.route,
            route = "auth"
        ) {
            composable(route = Screen.LoginScreen.route) {backStackEntry ->
                val viewModel: AuthenticationViewModel = viewModel(backStackEntry)
                RegisterScreen(viewModel, navController)
            }
        }
    }
}

private fun checkLoggedInState(): Boolean {
    var auth = FirebaseAuth.getInstance()
    return auth.currentUser != null
}