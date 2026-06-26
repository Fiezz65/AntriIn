package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StudentNotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<String>>(
        listOf(
            "Pesanan #ANT-092 kamu sudah siap diambil di Kasir!",
            "Hore! Pesanan Nasi Gorengmu sedang diproses oleh penjual.",
            "Pesanan #ANT-088 kamu telah selesai. Selamat menikmati!"
        )
    )
    val notifications: StateFlow<List<String>> = _notifications.asStateFlow()

    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}
