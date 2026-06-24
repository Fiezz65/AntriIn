package com.example.antriin.presentation.viewmodel.seller

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SellerProfileViewModel : ViewModel() {

    private val _sellerProfile = MutableStateFlow(User())
    val sellerProfile: StateFlow<User> = _sellerProfile

    private val _qrCodeUri = MutableStateFlow<Uri?>(null)
    val qrCodeUri: StateFlow<Uri?> = _qrCodeUri

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

    fun updateQrCode(uri: Uri?) {
        if (uri != null) {
            _qrCodeUri.value = uri
        }
    }

    fun updateProfile(newName: String, isOpen: Boolean) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {

            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .child("canteenName").setValue(newName)
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .child("isOpen").setValue(isOpen)
            

            _sellerProfile.value = _sellerProfile.value.copy(
                canteenName = newName,
                isOpen = isOpen
            )
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
