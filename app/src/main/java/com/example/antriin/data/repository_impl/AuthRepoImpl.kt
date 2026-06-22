package com.example.antriin.data.repository_impl

import com.example.antriin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepoImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) : AuthRepository {

    override suspend fun loginUser(email: String, pass: String, role: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
        // Here we could also verify if the user role matches what's in the DB
    }

    override suspend fun registerStudent(
        fullName: String,
        studentId: String,
        email: String,
        pass: String,
        phoneNumber: String,
        faculty: String,
        major: String
    ) {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        val userId = result.user?.uid ?: throw Exception("User creation failed")

        val userMap = mapOf(
            "uid" to userId,
            "fullName" to fullName,
            "studentId" to studentId,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "faculty" to faculty,
            "major" to major,
            "role" to "student"
        )

        db.getReference("users").child(userId).setValue(userMap).await()
    }

    override suspend fun registerSeller(
        canteenName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        location: String
    ) {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        val userId = result.user?.uid ?: throw Exception("User creation failed")

        val userMap = mapOf(
            "uid" to userId,
            "canteenName" to canteenName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "location" to location,
            "role" to "seller"
        )

        db.getReference("users").child(userId).setValue(userMap).await()
    }
}
