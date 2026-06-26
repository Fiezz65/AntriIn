package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.antriin.domain.repository.CartRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StudentProfileViewModel(private val cartRepository: CartRepository) : ViewModel() {

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

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                cartRepository.clearCart()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            FirebaseAuth.getInstance().signOut()
            onComplete()
        }
    }
}
