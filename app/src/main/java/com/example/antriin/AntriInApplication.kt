package com.example.antriin

import android.app.Application
import com.example.antriin.di.AppContainer
import com.example.antriin.di.DefaultAppContainer
import kotlinx.coroutines.launch

class AntriInApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}