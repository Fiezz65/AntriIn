package com.example.antriin.di

import android.content.Context
import com.example.antriin.data.local.AppDatabase
import com.example.antriin.data.repository_impl.AuthRepoImpl
import com.example.antriin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface AppContainer {
    val authRepository: AuthRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val appDatabase by lazy { AppDatabase.getDatabase(context) }

    override val authRepository: AuthRepository by lazy {
        AuthRepoImpl(
            firebaseAuth = FirebaseAuth.getInstance(),
            firebaseDatabase = FirebaseDatabase.getInstance()
        )
    }
}