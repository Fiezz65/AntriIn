package com.example.antriin.data.repository_impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.antriin.domain.model.User
import com.example.antriin.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : AuthRepository {

    override suspend fun loginUser(email: String, pass: String, role: String) {
        val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
        val uid = result.user?.uid ?: throw Exception("Login gagal")
        
        val snapshot = firebaseDatabase.getReference("users").child(uid).get().await()
        val userRole = snapshot.child("role").getValue(String::class.java)
        
        if (userRole != role) {
            firebaseAuth.signOut()
            throw Exception("Login ditolak! Akun ini terdaftar sebagai $userRole.")
        }
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
        val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
        val uid = result.user?.uid ?: throw Exception("Registrasi mahasiswa gagal")
        val user = User(
            uid = uid,
            fullName = fullName,
            email = email,
            role = "Mahasiswa",
            phoneNumber = phoneNumber,
            studentId = studentId,
            faculty = faculty,
            major = major
        )
        firebaseDatabase.getReference("users").child(uid).setValue(user).await()
    }

    override suspend fun registerSeller(
        canteenName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        location: String
    ) {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
        val uid = result.user?.uid ?: throw Exception("Registrasi penjual gagal")
        val user = User(
            uid = uid,
            fullName = canteenName,
            email = email,
            role = "Penjual",
            phoneNumber = phoneNumber,
            canteenName = canteenName,
            location = location
        )
        firebaseDatabase.getReference("users").child(uid).setValue(user).await()
    }

    override suspend fun checkUserRole(): String? {
        val user = firebaseAuth.currentUser ?: return null
        return try {
            val snapshot = firebaseDatabase.getReference("users").child(user.uid).get().await()
            snapshot.child("role").getValue(String::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}