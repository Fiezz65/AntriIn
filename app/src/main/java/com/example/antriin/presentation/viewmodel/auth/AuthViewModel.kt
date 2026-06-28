package com.example.antriin.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.repository.AuthRepository
import com.example.antriin.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val authState: StateFlow<UiState<Unit>> = _authState

    fun loginUser(email: String, pass: String, role: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = UiState.Error("Email dan kata sandi tidak boleh kosong")
            return
        }
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                authRepository.loginUser(email, pass, role)
                _authState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Gagal masuk")
            }
        }
    }

    fun registerStudent(
        fullName: String,
        studentId: String,
        email: String,
        pass: String,
        phoneNumber: String,
        faculty: String,
        major: String
    ) {
        if (fullName.isBlank() || studentId.isBlank() || email.isBlank() || pass.isBlank() || phoneNumber.isBlank() || faculty.isBlank() || major.isBlank()) {
            _authState.value = UiState.Error("Semua kolom harus diisi")
            return
        }
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                authRepository.registerStudent(fullName, studentId, email, pass, phoneNumber, faculty, major)
                _authState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Gagal mendaftar")
            }
        }
    }

    fun registerSeller(
        canteenName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        location: String,
        secretCode: String
    ) {
        if (canteenName.isBlank() || email.isBlank() || pass.isBlank() || phoneNumber.isBlank() || location.isBlank() || secretCode.isBlank()) {
            _authState.value = UiState.Error("Semua kolom harus diisi")
            return
        }
        
        if (secretCode != "KANTIN-123") {
            _authState.value = UiState.Error("Kode akses rahasia kantin salah!")
            return
        }
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                authRepository.registerSeller(canteenName, email, pass, phoneNumber, location)
                _authState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Gagal mendaftar")
            }
        }
    }

    fun resetState() {
        _authState.value = UiState.Idle
    }
}