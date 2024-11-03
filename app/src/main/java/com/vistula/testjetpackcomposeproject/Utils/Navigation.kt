package com.vistula.testjetpackcomposeproject.Utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vistula.testjetpackcomposeproject.ItemViewModel
import com.vistula.testjetpackcomposeproject.Screens.AddScreen
import com.vistula.testjetpackcomposeproject.Screens.LoginScreen
import com.vistula.testjetpackcomposeproject.Screens.MainScreen

@Composable
fun Navigation(
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.AddScreen.route) {
        navigation(
            startDestination = Screen.MainScreen.route,
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
                val viewModel: LoginScreenViewModel = viewModel(backStackEntry)
                LoginScreen(viewModel)
            }
        }
    }
}