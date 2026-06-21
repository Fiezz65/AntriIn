package com.example.antriin.presentation.seller

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SellerProfileViewModel : ViewModel() {

    private val _sellerProfile = MutableStateFlow(
        User(
            canteenName = "Kantin Teknik TI",
            location = "Fakultas Teknik (Banjarmasin)",
            role = "Penjual",
            isOpen = true
        )
    )
    val sellerProfile: StateFlow<User> = _sellerProfile

    private val _qrCodeUri = MutableStateFlow<Uri?>(null)
    val qrCodeUri: StateFlow<Uri?> = _qrCodeUri

    fun updateQrCode(uri: Uri?) {
        if (uri != null) {
            _qrCodeUri.value = uri
        }
    }

    fun updateProfile(newName: String, isOpen: Boolean) {
        _sellerProfile.value = _sellerProfile.value.copy(
            canteenName = newName,
            isOpen = isOpen
        )
    }

    fun logout() {

    }
}