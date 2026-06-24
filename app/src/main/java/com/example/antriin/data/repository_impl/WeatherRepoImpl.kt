package com.example.antriin.data.repository_impl

import com.example.antriin.domain.model.WeatherInfo
import com.example.antriin.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class WeatherRepoImpl : WeatherRepository {
    override suspend fun getCurrentWeather(city: String): WeatherInfo = withContext(Dispatchers.IO) {
        val lat = if (city.equals("Banjarbaru", ignoreCase = true)) "-3.4413" else "-3.3167"
        val lon = if (city.equals("Banjarbaru", ignoreCase = true)) "114.8272" else "114.5833"
        try {
            val url = URL("https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val current = json.getJSONObject("current_weather")
                val temp = current.getDouble("temperature")
                val isDay = current.getInt("is_day") == 1
                
                WeatherInfo(
                    temperature = "${temp}°C",
                    description = if (isDay) "Cerah / Siang" else "Malam",
                    city = city
                )
            } else {
                WeatherInfo("??°C", "Gagal memuat cuaca", city)
            }
        } catch (e: Exception) {
            WeatherInfo("??°C", "Koneksi bermasalah", city)
        }
    }
}
