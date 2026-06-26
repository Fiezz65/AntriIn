package com.example.antriin.domain.model

data class WeatherInfo(
    val temperature: String = "",
    val description: String = "",
    val city: String = "",
    val emoji: String = "☀️"
)