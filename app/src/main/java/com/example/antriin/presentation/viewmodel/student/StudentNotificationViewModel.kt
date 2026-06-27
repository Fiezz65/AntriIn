package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudentNotificationViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val notifications: StateFlow<List<Pair<String, String>>> = _notifications
    
    val unreadCount: StateFlow<Int> = com.example.antriin.utils.NotificationState.studentUnreadCount

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getStudentOrders(user.uid).collectLatest { orders ->
                    val notifList = mutableListOf<Pair<String, String>>()
                    orders.forEach { order ->
                        when (order.status) {
                            "Belum Bayar", "Menunggu Validasi" -> {
                                if (order.paymentMethod == "Tunai") {
                                    notifList.add(Pair("Menunggu Pembayaran", "Pesanan baru Anda berhasil dibuat. Silakan segera ke kantin untuk membayar secara Tunai agar pesanan divalidasi."))
                                } else {
                                    notifList.add(Pair("Menunggu Pembayaran", "Pesanan baru Anda berhasil dibuat. Silakan selesaikan pembayaran via ${order.paymentMethod}."))
                                }
                            }
                            "Diproses" -> notifList.add(Pair("Pesanan Diproses", "Hore! Pesanan Anda sedang diproses oleh penjual."))
                            "Siap Diambil" -> notifList.add(Pair("Pesanan Siap Diambil!", "Pesanan Anda sudah siap diambil di Kantin!"))
                            "Selesai" -> notifList.add(Pair("Pesanan Selesai", "Pesanan Anda telah selesai. Selamat menikmati!"))
                            "Dibatalkan" -> notifList.add(Pair("Pesanan Dibatalkan", "Pesanan Anda dibatalkan oleh penjual."))
                        }
                    }
                    _notifications.value = notifList.take(20)
                    com.example.antriin.utils.NotificationState.studentUnreadCount.value = orderRepository.getUnreadCount(orders, "student")
                }
            }
        }
    }

    fun markAsRead() {
        orderRepository.markAsRead("student")
        com.example.antriin.utils.NotificationState.studentUnreadCount.value = 0
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}
