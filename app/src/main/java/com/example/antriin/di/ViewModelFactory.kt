package com.example.antriin.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.antriin.AntriInApplication
import com.example.antriin.presentation.auth.AuthViewModel
import com.example.antriin.presentation.seller.MenuViewModel

object ViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            AuthViewModel(authRepository = application.container.authRepository)
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            MenuViewModel(
                menuRepository = application.container.menuRepository
            )
        }
    }
}