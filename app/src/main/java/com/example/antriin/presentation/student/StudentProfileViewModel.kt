package com.example.antriin.presentation.student

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StudentProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(User())
    val userProfile: StateFlow<User> = _userProfile

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        _userProfile.value = user
                    }
                }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}