package com.example.antriin.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Notification : Screen("notification")
    object Cart : Screen("cart")
    object Tracking : Screen("tracking")
    object Profile : Screen("profile")
}