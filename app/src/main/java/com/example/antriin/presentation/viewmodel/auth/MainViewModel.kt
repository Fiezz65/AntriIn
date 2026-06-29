package com.example.antriin.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.usecase.CheckUserRoleUseCase
import com.example.antriin.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val checkUserRoleUseCase: CheckUserRoleUseCase,
    private val cartRepository: com.example.antriin.domain.repository.CartRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        checkDestination()
    }

    private fun checkDestination() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(500)
            val role = checkUserRoleUseCase()
            _startDestination.value = when (role) {
                "Mahasiswa" -> Screen.Home.route
                "Penjual" -> Screen.Dashboard.route
                else -> Screen.Login.route
            }
        }
    }
}