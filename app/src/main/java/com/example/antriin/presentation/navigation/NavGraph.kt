package com.example.antriin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.antriin.presentation.ui.auth.LoginScreen
import com.example.antriin.presentation.ui.auth.RegisterScreen
import com.example.antriin.presentation.ui.seller.DashboardScreen
import com.example.antriin.presentation.ui.seller.HistoryScreen
import com.example.antriin.presentation.ui.seller.MenuScreen
import com.example.antriin.presentation.ui.seller.SellerNotificationScreen
import com.example.antriin.presentation.ui.seller.SellerProfileScreen
import com.example.antriin.presentation.ui.student.CartScreen
import com.example.antriin.presentation.ui.student.HomeScreen
import com.example.antriin.presentation.ui.student.LiveTrackingScreen
import com.example.antriin.presentation.ui.student.StudentNotificationScreen
import com.example.antriin.presentation.ui.student.StudentProfileScreen

@Composable
fun SetupNavGraph(navController: NavHostController, startDestination: String = Screen.Login.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { role ->
                    if (role == "Mahasiswa") {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { role ->
                    if (role == "Mahasiswa") {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }
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
                onNavigate = { route -> 
                    if (route == "login") {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(route) 
                    }
                },
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
                onNavigate = { route -> 
                    if (route == "login") {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(route) 
                    }
                },
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
