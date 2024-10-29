package com.vistula.testjetpackcomposeproject.Utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vistula.testjetpackcomposeproject.ItemViewModel
import com.vistula.testjetpackcomposeproject.Screens.AddScreen
import com.vistula.testjetpackcomposeproject.Screens.MainScreen

@Composable
fun Navigation(
    viewModel: ItemViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.AddScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(viewModel)
        }

        composable(route = Screen.AddScreen.route) {
            AddScreen(viewModel, navController)
        }
    }
}