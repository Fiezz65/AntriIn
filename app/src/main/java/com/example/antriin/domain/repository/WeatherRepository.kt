package com.example.antriin.domain.repository

import com.example.antriin.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): WeatherInfo
}