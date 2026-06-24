package com.example.antriin.di

import android.content.Context
import com.example.antriin.data.local.AppDatabase
import com.example.antriin.data.repository_impl.AuthRepoImpl
import com.example.antriin.data.repository_impl.MenuRepoImpl
import com.example.antriin.domain.repository.AuthRepository
import com.example.antriin.domain.repository.MenuRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface AppContainer {
    val authRepository: AuthRepository
    val menuRepository: MenuRepository
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
}