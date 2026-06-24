package com.example.antriin.domain.repository

interface AuthRepository {
    suspend fun loginUser(email: String, pass: String, role: String)

    suspend fun registerStudent(
        fullName: String,
        studentId: String,
        email: String,
        pass: String,
        phoneNumber: String,
        faculty: String,
        major: String
    )

    suspend fun registerSeller(
        canteenName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        location: String
    )

    suspend fun checkUserRole(): String?
}