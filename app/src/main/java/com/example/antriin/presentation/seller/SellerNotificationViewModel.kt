package com.example.antriin.presentation.seller

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SellerNotificationViewModel : ViewModel() {

    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications: StateFlow<List<String>> = _notifications

    init {
        loadDummyNotifications()
    }

    private fun loadDummyNotifications() {
        _notifications.value = listOf(
            "Pesanan baru #ANT-003 dari Andi K. Metode: QRIS.",
            "Pesanan #ANT-002 dari Siti M. menunggu validasi pembayaran.",
            "Pesanan #ANT-001 berhasil diselesaikan."
        )
    }
}