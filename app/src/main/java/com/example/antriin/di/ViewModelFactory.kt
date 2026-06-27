package com.example.antriin.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.antriin.AntriInApplication
import com.example.antriin.presentation.viewmodel.auth.AuthViewModel
import com.example.antriin.presentation.viewmodel.seller.MenuViewModel
import com.example.antriin.presentation.viewmodel.student.CartViewModel
import com.example.antriin.presentation.viewmodel.student.HomeViewModel

object ViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            AuthViewModel(authRepository = application.container.authRepository)
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.auth.MainViewModel(
                checkUserRoleUseCase = application.container.checkUserRoleUseCase,
                cartRepository = application.container.cartRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            MenuViewModel(
                menuRepository = application.container.menuRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            HomeViewModel(
                menuRepository = application.container.menuRepository,
                weatherRepository = application.container.weatherRepository,
                userRepository = application.container.userRepository,
                orderRepository = application.container.orderRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            CartViewModel(
                cartRepository = application.container.cartRepository,
                menuRepository = application.container.menuRepository,
                orderRepository = application.container.orderRepository,
                userRepository = application.container.userRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.student.StudentProfileViewModel(
                cartRepository = application.container.cartRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.seller.SellerProfileViewModel(
                cartRepository = application.container.cartRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.seller.DashboardViewModel(
                orderRepository = application.container.orderRepository,
                userRepository = application.container.userRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.student.StudentNotificationViewModel(
                orderRepository = application.container.orderRepository,
                userRepository = application.container.userRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.seller.SellerNotificationViewModel(
                orderRepository = application.container.orderRepository,
                userRepository = application.container.userRepository
            )
        }
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AntriInApplication)
            com.example.antriin.presentation.viewmodel.student.LiveTrackingViewModel(
                orderRepository = application.container.orderRepository,
                userRepository = application.container.userRepository
            )
        }
    }
}
