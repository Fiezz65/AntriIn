package com.example.antriin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.antriin.presentation.seller.DashboardScreen
import com.example.antriin.presentation.seller.HistoryScreen
import com.example.antriin.presentation.seller.MenuScreen
import com.example.antriin.presentation.seller.SellerNotificationScreen
import com.example.antriin.presentation.seller.SellerProfileScreen
import com.example.antriin.presentation.student.CartScreen
import com.example.antriin.presentation.student.HomeScreen
import com.example.antriin.presentation.student.LiveTrackingScreen
import com.example.antriin.presentation.student.StudentNotificationScreen
import com.example.antriin.presentation.student.StudentProfileScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.Notification.route) {
            StudentNotificationScreen(onBackClick = { navController.popBackStack() })
        }
        composable(route = Screen.Cart.route) {
            CartScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.Tracking.route) {
            LiveTrackingScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.Profile.route) {
            StudentProfileScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.SellerNotification.route) {
            SellerNotificationScreen(onBackClick = { navController.popBackStack() })
        }
        composable(route = Screen.Menu.route) {
            MenuScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.History.route) {
            HistoryScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.SellerProfile.route) {
            SellerProfileScreen(
                onNavigate = { route -> navController.navigate(route) },
                onTabNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}