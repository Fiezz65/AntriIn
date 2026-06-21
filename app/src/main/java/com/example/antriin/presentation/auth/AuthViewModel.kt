package com.example.antriin.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun loginUser(email: String, pass: String, role: String) {

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

    }

    fun registerSeller(
        canteenName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        location: String
    ) {

    }
}