package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.antriin.utils.formatMenuNames

class StudentNotificationViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Triple<String, String, Boolean>>>(emptyList())
    val notifications: StateFlow<List<Triple<String, String, Boolean>>> = _notifications
    
    val unreadCount: StateFlow<Int> = com.example.antriin.utils.NotificationState.studentUnreadCount

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getStudentOrders(user.uid).collectLatest { orders ->
                    val lastReadTime = orderRepository.getLastReadTime("student")
                    val notifList = mutableListOf<Triple<String, String, Boolean>>()
                    
                    val calendar = java.util.Calendar.getInstance()
                    val currentYear = calendar.get(java.util.Calendar.YEAR)
                    val currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)

                    orders.forEach { order ->
                        calendar.timeInMillis = order.timestamp
                        val isToday = calendar.get(java.util.Calendar.YEAR) == currentYear && 
                                      calendar.get(java.util.Calendar.DAY_OF_YEAR) == currentDay
                                      
                        if (isToday) {
                            val menuNames = order.items.formatMenuNames()
                            val isUnread = order.lastUpdated > lastReadTime
                            when (order.status) {
                                "Belum Bayar", "Menunggu Validasi" -> {
                                    if (order.paymentMethod == "Tunai") {
                                        notifList.add(Triple("Menunggu Pembayaran", "Pesanan $menuNames Anda berhasil dibuat. Silakan segera ke kantin untuk membayar secara Tunai agar pesanan divalidasi.", isUnread))
                                    } else {
                                        notifList.add(Triple("Menunggu Pembayaran", "Pesanan $menuNames Anda berhasil dibuat. Silakan selesaikan pembayaran via ${order.paymentMethod}.", isUnread))
                                    }
                                }
                                "Diproses" -> notifList.add(Triple("Pesanan Diproses", "Hore! Pesanan $menuNames Anda sedang diproses oleh penjual.", isUnread))
                                "Siap Diambil", "Selesai" -> notifList.add(Triple("Pesanan Siap Diambil!", "Pesanan $menuNames Anda selesai diproses dan siap diambil. Silakan ambil di Kantin!", isUnread))
                                "Dibatalkan" -> notifList.add(Triple("Pesanan Dibatalkan", "Pesanan $menuNames Anda dibatalkan oleh penjual.", isUnread))
                            }
                        }
                    }
                    _notifications.value = notifList.take(20)
                    com.example.antriin.utils.NotificationState.studentUnreadCount.value = notifList.count { it.third }
                }
            }
        }
    }

    fun markAsRead() {
        orderRepository.markAsRead("student")
        com.example.antriin.utils.NotificationState.studentUnreadCount.value = 0
    }

    fun clearBadge() {
        com.example.antriin.utils.NotificationState.studentUnreadCount.value = 0
    }



    private var isListenerStarted = false

    fun startGlobalListener(context: android.content.Context) {
        if (isListenerStarted) return
        isListenerStarted = true
        var isInitialLoad = true
        
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getStudentOrders(user.uid).collectLatest { orders ->
                    val calendar = java.util.Calendar.getInstance()
                    val currentYear = calendar.get(java.util.Calendar.YEAR)
                    val currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)

                    orders.forEach { order ->
                        calendar.timeInMillis = order.timestamp
                        val isToday = calendar.get(java.util.Calendar.YEAR) == currentYear && 
                                      calendar.get(java.util.Calendar.DAY_OF_YEAR) == currentDay

                        if (isToday) {
                            val statusKey = "${order.orderId}_${order.status}"
                            if (!com.example.antriin.utils.NotificationState.notifiedStudentOrderStatuses.contains(statusKey)) {
                                com.example.antriin.utils.NotificationState.notifiedStudentOrderStatuses.add(statusKey)
                                
                                val lastReadTime = orderRepository.getLastReadTime("student")
                                if (!isInitialLoad || order.lastUpdated > lastReadTime) {
                                    val missedSiapDiambil = order.status == "Selesai" && !com.example.antriin.utils.NotificationState.notifiedStudentOrderStatuses.contains("${order.orderId}_Siap Diambil")
                                    if (order.status != "Selesai" || missedSiapDiambil) {
                                        if (missedSiapDiambil) {
                                            com.example.antriin.utils.NotificationState.notifiedStudentOrderStatuses.add("${order.orderId}_Siap Diambil")
                                        }
                                        val menuNames = order.items.formatMenuNames()
                                        val title = when (order.status) {
                                            "Menunggu Validasi", "Belum Bayar" -> "Pesanan Dibuat"
                                            "Diproses" -> "Pesanan Diproses"
                                            "Siap Diambil", "Selesai" -> "Pesanan Siap Diambil!"
                                            "Dibatalkan" -> "Pesanan Dibatalkan"
                                            else -> "Update Pesanan"
                                        }
                                        val msg = when (order.status) {
                                            "Menunggu Validasi", "Belum Bayar" -> "Pesanan $menuNames Anda sedang menunggu proses."
                                            "Diproses" -> "Hore! Pesanan $menuNames Anda sedang diproses oleh penjual."
                                            "Siap Diambil", "Selesai" -> "Pesanan $menuNames Anda selesai diproses dan siap diambil. Silakan ambil di Kantin!"
                                            "Dibatalkan" -> "Pesanan $menuNames Anda dibatalkan oleh penjual."
                                            else -> "Status pesanan $menuNames Anda telah diperbarui."
                                        }
                                        com.example.antriin.utils.NotificationHelper.showStudentOrderStatusNotification(context, title, msg)
                                    }
                                }
                            }
                        }
                    }
                    isInitialLoad = false
                }
            }
        }
    }
}