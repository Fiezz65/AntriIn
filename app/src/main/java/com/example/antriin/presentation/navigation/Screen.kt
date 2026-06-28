package com.example.antriin.presentation.navigation
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")

    object Home : Screen("home")
    object Cart : Screen("cart")
    object Tracking : Screen("tracking")
    object Profile : Screen("profile")
    object Notification : Screen("notification")

    object Dashboard : Screen("dashboard")
    object Menu : Screen("menu")
    object History : Screen("history")
    object SellerNotification : Screen("seller_notification")
    object SellerProfile : Screen("seller_profile")
}