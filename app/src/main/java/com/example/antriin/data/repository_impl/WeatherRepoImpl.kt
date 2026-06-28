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
                val weatherCode = if (current.has("weathercode")) current.getInt("weathercode") else 0
                
                val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
                val timeOfDay = when (hour) {
                    in 5..10 -> "Pagi"
                    in 11..14 -> "Siang"
                    in 15..17 -> "Sore"
                    else -> "Malam"
                }
                
                val (condition, emoji) = when (weatherCode) {
                    0 -> "Cerah" to if (isDay) "☀️" else "🌕"
                    1, 2 -> "Cerah Berawan" to if (isDay) "⛅" else "☁️"
                    3 -> "Mendung" to "☁️"
                    45, 48 -> "Berkabut" to "🌫️"
                    51, 53, 55, 56, 57 -> "Gerimis" to "🌧️"
                    61, 63, 65, 66, 67 -> "Hujan" to "🌧️"
                    80, 81, 82 -> "Hujan Lebat" to "🌧️"
                    95, 96, 99 -> "Badai Petir" to "⛈️"
                    else -> "Tidak Menentu" to if (isDay) "☀️" else "🌕"
                }

                WeatherInfo(
                    temperature = "${temp}°C",
                    description = "$timeOfDay / $condition",
                    city = city,
                    emoji = emoji
                )
            } else {
                WeatherInfo("??°C", "Gagal memuat cuaca", city)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            WeatherInfo("??°C", "Koneksi bermasalah", city)
        }
    }
}