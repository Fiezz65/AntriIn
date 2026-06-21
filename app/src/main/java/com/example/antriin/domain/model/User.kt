package com.example.antriin.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "",
    val phoneNumber: String = "",
    val studentId: String = "",
    val faculty: String = "",
    val major: String = "",
    val canteenName: String = "",
    val location: String = "",
    val isOpen: Boolean = true
)