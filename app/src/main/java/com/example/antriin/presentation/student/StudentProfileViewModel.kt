package com.example.antriin.presentation.student

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StudentProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(
        User(
            fullName = "Andi Pratama",
            studentId = "2101234567",
            email = "andi.pratama@student.univ.edu",
            phoneNumber = "081234567890",
            role = "Mahasiswa"
        )
    )
    val userProfile: StateFlow<User> = _userProfile

    fun logout() {

    }
}