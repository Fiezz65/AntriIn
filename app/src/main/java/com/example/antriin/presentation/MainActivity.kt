package com.example.antriin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.antriin.presentation.navigation.Screen
import com.example.antriin.presentation.navigation.SetupNavGraph
import com.example.antriin.presentation.theme.AntriInTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntriInTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }
                
                LaunchedEffect(Unit) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        try {
                            val snapshot = FirebaseDatabase.getInstance().getReference("users").child(user.uid).get().await()
                            val role = snapshot.child("role").getValue(String::class.java)
                            startDestination = if (role == "Mahasiswa") Screen.Home.route else Screen.Dashboard.route
                        } catch (e: Exception) {
                            startDestination = Screen.Login.route
                        }
                    } else {
                        startDestination = Screen.Login.route
                    }
                }
                
                if (startDestination == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    SetupNavGraph(navController = navController, startDestination = startDestination!!)
                }
            }
        }
    }
}