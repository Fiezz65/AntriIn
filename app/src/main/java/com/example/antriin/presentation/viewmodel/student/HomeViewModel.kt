package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.model.WeatherInfo
import com.example.antriin.domain.repository.MenuRepository
import com.example.antriin.domain.repository.UserRepository
import com.example.antriin.domain.repository.WeatherRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.antriin.presentation.viewmodel.seller.MenuState

import com.example.antriin.domain.repository.OrderRepository

class HomeViewModel(
    private val menuRepository: MenuRepository,
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _weatherInfo = MutableStateFlow(WeatherInfo("-", "Memuat..."))
    val weatherInfo: StateFlow<WeatherInfo> = _weatherInfo

    private val _crowdCount = MutableStateFlow(0)
    val crowdCount: StateFlow<Int> = _crowdCount

    private val _menuList = MutableStateFlow(MenuState())
    val menuList: StateFlow<MenuState> = _menuList

    private val _locations = MutableStateFlow<List<String>>(emptyList())
    val locations: StateFlow<List<String>> = _locations

    private val _selectedLocation = MutableStateFlow("")
    val selectedLocation: StateFlow<String> = _selectedLocation

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val locationCityMap = mapOf(
        "Fakultas Teknik (Banjarmasin)" to "Banjarmasin",
        "Fakultas Teknik (Banjarbaru)" to "Banjarbaru",
        "Fakultas Ekonomi dan Bisnis" to "Banjarmasin",
        "Fakultas Hukum" to "Banjarmasin",
        "Fakultas Pertanian" to "Banjarbaru",
        "Fakultas Kehutanan" to "Banjarbaru",
        "Fakultas Perikanan" to "Banjarbaru"
    )

    init {
        fetchUserData()
        loadLocations()
        fetchMenus()
    }

    private fun fetchUserData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    _userName.value = snapshot.child("fullName").getValue(String::class.java) ?: "Mahasiswa"
                    val faculty = snapshot.child("faculty").getValue(String::class.java)
                    if (!faculty.isNullOrEmpty() && locationCityMap.containsKey(faculty)) {
                        _selectedLocation.value = faculty
                    }
                }
        }
    }

    private fun loadLocations() {
        val fetchedLocations = listOf("Belum Dipilih") + locationCityMap.keys.toList()
        _locations.value = fetchedLocations

        if (fetchedLocations.isNotEmpty()) {
            _selectedLocation.value = fetchedLocations[0]
        }
    }

    private var fetchJob: Job? = null

    private fun fetchMenus() {
        viewModelScope.launch {
            _selectedLocation.collect { location ->
                fetchJob?.cancel()
                if (location.isNotEmpty() && location != "Belum Dipilih") {
                    fetchWeather(location)
                    fetchJob = launch {
                        try {
                            kotlinx.coroutines.flow.combine(
                                userRepository.getSellersByLocation(location),
                                menuRepository.getAllMenus()
                            ) { sellers, menus ->
                                val sellerMap = sellers.associateBy { it.uid }
                                menus.filter { sellerMap.containsKey(it.sellerId) }
                                    .map { 
                                        val seller = sellerMap[it.sellerId]
                                        val m = it.copy(canteenName = seller?.canteenName ?: "")
                                        m.isCanteenOpen = seller?.isOpen ?: true
                                        m
                                    }
                            }.collect { combinedMenus ->
                                _menuList.value = MenuState(combinedMenus, System.currentTimeMillis())
                            }
                        } catch (e: Exception) {
                        }
                    }

                    launch {
                        userRepository.getSellersByLocation(location).collect { sellers ->
                            val sellerQueueMap = mutableMapOf<String, Int>()
                            sellers.forEach { seller ->
                                launch {
                                    orderRepository.getSellerOrders(seller.uid).collect { orders ->
                                        val activeOrders = orders.count {
                                            it.status == "Menunggu Validasi" || it.status == "Diproses" || it.status == "Siap Diambil"
                                        }
                                        sellerQueueMap[seller.uid] = activeOrders
                                        _crowdCount.value = sellerQueueMap.values.sum()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    _menuList.value = MenuState()
                    _weatherInfo.value = WeatherInfo("-", "Pilih lokasi", "-")
                    _crowdCount.value = 0
                }
            }
        }
    }

    private fun fetchWeather(location: String = "Fakultas Teknik (Banjarmasin)") {
        val city = locationCityMap[location] ?: "Banjarmasin"
        viewModelScope.launch {
            try {
                val info = weatherRepository.getCurrentWeather(city)
                _weatherInfo.value = info
            } catch (e: Exception) {
                _weatherInfo.value = WeatherInfo("??°C", "Koneksi bermasalah", city)
            }
        }
    }

    fun updateSelectedLocation(location: String) {
        _selectedLocation.value = location
    }
}
