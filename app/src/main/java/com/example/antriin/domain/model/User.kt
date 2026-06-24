package com.example.antriin.domain.model

import com.google.firebase.database.PropertyName

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
    @get:PropertyName("isOpen")
    @set:PropertyName("isOpen")
    var isOpen: Boolean = true
)