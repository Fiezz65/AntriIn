package com.example.antriin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.antriin.presentation.auth.LoginScreen
import com.example.antriin.presentation.auth.RegisterScreen
import com.example.antriin.presentation.theme.AntriInTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntriInTheme {
                var showLogin by remember { mutableStateOf(true) }

                if (showLogin) {
                    LoginScreen(
                        onNavigateToRegister = { showLogin = false },
                        onLoginClick = { email, password, role -> }
                    )
                } else {
                    RegisterScreen(
                        onNavigateToLogin = { showLogin = true },
                        onRegisterStudentClick = { fullName, studentId, email, password, phoneNumber, faculty, major -> },
                        onRegisterSellerClick = { canteenName, email, password, phoneNumber, location -> }
                    )
                }
            }
        }
    }
}