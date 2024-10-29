package com.vistula.testjetpackcomposeproject.Utils

sealed class Screen(val route: String) {
    data object MainScreen: Screen("main_screen")
    data object AddScreen: Screen("add_screen")
}