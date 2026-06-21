package com.example.antriin.di

import android.content.Context

interface AppContainer {

}

class DefaultAppContainer(private val context: Context) : AppContainer {

}