package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.antriin.domain.repository.CartRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SellerProfileViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private val _sellerProfile = MutableStateFlow(User())
    val sellerProfile: StateFlow<User> = _sellerProfile

    init {
        fetchSellerProfile()
    }

    private fun fetchSellerProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        _sellerProfile.value = user
                    }
                }
        }
    }

    fun updateProfile(newName: String, isOpen: Boolean, paymentInfo: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {

            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .child("canteenName").setValue(newName)
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .child("isOpen").setValue(isOpen)
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .child("paymentInfo").setValue(paymentInfo)
            
            _sellerProfile.value = _sellerProfile.value.copy(
                canteenName = newName,
                isOpen = isOpen,
                paymentInfo = paymentInfo
            )
        }
    }

    fun updatePaymentInfo(canteenName: String, isOpen: Boolean, selectedMethods: List<String>, phoneNumber: String) {
        val newPaymentInfo = if (selectedMethods.isEmpty() || phoneNumber.isEmpty()) {
            ""
        } else {
            "${selectedMethods.joinToString(", ")} - $phoneNumber"
        }
        updateProfile(canteenName, isOpen, newPaymentInfo)
    }

    fun logout() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
        FirebaseAuth.getInstance().signOut()
    }
}