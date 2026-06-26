package com.example.antriin.di

import android.content.Context
import com.example.antriin.data.local.AppDatabase
import com.example.antriin.data.repository_impl.AuthRepoImpl
import com.example.antriin.data.repository_impl.CartRepoImpl
import com.example.antriin.data.repository_impl.MenuRepoImpl
import com.example.antriin.data.repository_impl.OrderRepoImpl
import com.example.antriin.data.repository_impl.WeatherRepoImpl
import com.example.antriin.domain.repository.AuthRepository
import com.example.antriin.domain.repository.CartRepository
import com.example.antriin.domain.repository.MenuRepository
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import com.example.antriin.domain.repository.WeatherRepository
import com.example.antriin.domain.usecase.CheckUserRoleUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface AppContainer {
    val authRepository: AuthRepository
    val menuRepository: MenuRepository
    val cartRepository: CartRepository
    val orderRepository: OrderRepository
    val weatherRepository: WeatherRepository
    val userRepository: UserRepository
    val checkUserRoleUseCase: CheckUserRoleUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val appDatabase by lazy { AppDatabase.getDatabase(context) }

    override val authRepository: AuthRepository by lazy {
        AuthRepoImpl(
            firebaseAuth = FirebaseAuth.getInstance(),
            firebaseDatabase = FirebaseDatabase.getInstance()
        )
    }

    override val menuRepository: MenuRepository by lazy {
        MenuRepoImpl(
            database = FirebaseDatabase.getInstance()
        )
    }

    override val checkUserRoleUseCase: CheckUserRoleUseCase by lazy {
        CheckUserRoleUseCase(authRepository)
    }

    override val cartRepository: CartRepository by lazy {
        CartRepoImpl(cartDao = appDatabase.cartDao())
    }

    override val orderRepository: OrderRepository by lazy {
        OrderRepoImpl()
    }

    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepoImpl()
    }

    override val userRepository: UserRepository by lazy {
        com.example.antriin.data.repository_impl.UserRepoImpl()
    }
}