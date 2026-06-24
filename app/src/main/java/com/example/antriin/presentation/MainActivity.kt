package com.example.antriin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.antriin.presentation.navigation.Screen
import com.example.antriin.presentation.navigation.SetupNavGraph
import com.example.antriin.presentation.theme.AntriInTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.di.ViewModelFactory
import com.example.antriin.presentation.viewmodel.auth.MainViewModel
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntriInTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel(factory = ViewModelFactory.Factory)
                val startDestination by mainViewModel.startDestination.collectAsState()
                
                if (startDestination == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    startDestination?.let { dest ->
                        SetupNavGraph(navController = navController, startDestination = dest)
                    }
                }
            }
        }
    }
}