package com.example.antriin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import com.example.antriin.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.antriin.presentation.navigation.SetupNavGraph
import com.example.antriin.presentation.theme.AntriInTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.di.ViewModelFactory
import com.example.antriin.presentation.viewmodel.auth.MainViewModel
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                androidx.core.app.ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        setContent {
            AntriInTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel(factory = ViewModelFactory.Factory)
                val startDestination by mainViewModel.startDestination.collectAsState()
                
                if (startDestination == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_antriin_logo_transparent),
                            contentDescription = "Logo AntriIn",
                            modifier = Modifier.size(150.dp)
                        )
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