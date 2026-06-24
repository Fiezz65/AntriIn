package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.model.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _weatherInfo = MutableStateFlow(WeatherInfo("32°C", "Siang yang terik!"))
    val weatherInfo: StateFlow<WeatherInfo> = _weatherInfo

    private val _isCrowded = MutableStateFlow(true)
    val isCrowded: StateFlow<Boolean> = _isCrowded

    private val _menuList = MutableStateFlow<List<Menu>>(emptyList())
    val menuList: StateFlow<List<Menu>> = _menuList

    private val _locations = MutableStateFlow<List<String>>(emptyList())
    val locations: StateFlow<List<String>> = _locations

    private val _selectedLocation = MutableStateFlow("")
    val selectedLocation: StateFlow<String> = _selectedLocation

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        val fetchedLocations = listOf(
            "Fakultas Teknik (Banjarmasin)",
            "Fakultas Teknik (Banjarbaru)",
            "Fakultas Ekonomi dan Bisnis",
            "Fakultas Hukum",
            "Fakultas Pertanian",
            "Fakultas Kehutanan",
            "Fakultas Perikanan"
        )
        _locations.value = fetchedLocations

        if (fetchedLocations.isNotEmpty()) {
            _selectedLocation.value = fetchedLocations[0]
        }

        _menuList.value = listOf(
            Menu(id = "1", name = "Nasi Goreng Spesial", description = "Warung Pak Kumis", price = 15000, category = "Nasi"),
            Menu(id = "2", name = "Mie Ayam Bakso", description = "Mie Ayam Mas Bro", price = 12000, category = "Mie"),
            Menu(id = "3", name = "Es Teh Manis", description = "Kedai Minum Haus", price = 4000, category = "Minuman")
        )
    }

    fun updateSelectedLocation(location: String) {
        _selectedLocation.value = location
    }
}
