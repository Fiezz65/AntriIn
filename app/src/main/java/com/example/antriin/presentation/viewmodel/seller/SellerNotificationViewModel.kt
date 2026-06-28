package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.antriin.utils.formatMenuNames

class SellerNotificationViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Triple<String, String, Boolean>>>(emptyList())
    val notifications: StateFlow<List<Triple<String, String, Boolean>>> = _notifications
    
    val unreadCount: StateFlow<Int> = com.example.antriin.utils.NotificationState.sellerUnreadCount

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getSellerOrders(user.uid).collectLatest { orders ->
                    val lastReadTime = orderRepository.getLastReadTime("seller")
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
                                "Menunggu Validasi" -> notifList.add(Triple("Pesanan Baru!", "Pesanan baru dari ${order.buyerName} ($menuNames, ${order.paymentMethod}) menunggu validasi.", isUnread))
                                "Belum Bayar" -> notifList.add(Triple("Menunggu Pembayaran", "Pesanan dari ${order.buyerName} ($menuNames, ${order.paymentMethod}) belum dibayar.", isUnread))
                            }
                        }
                    }
                    _notifications.value = notifList.take(20)
                    com.example.antriin.utils.NotificationState.sellerUnreadCount.value = notifList.count { it.third }
                }
            }
        }
    }
    
    fun markAsRead() {
        orderRepository.markAsRead("seller")
        com.example.antriin.utils.NotificationState.sellerUnreadCount.value = 0
    }

    fun clearBadge() {
        com.example.antriin.utils.NotificationState.sellerUnreadCount.value = 0
    }
    
    private var isListenerStarted = false

    fun startGlobalListener(context: android.content.Context) {
        if (isListenerStarted) return
        isListenerStarted = true
        var isInitialLoad = true
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getSellerOrders(user.uid).collectLatest { orders ->
                    val lastReadTime = orderRepository.getLastReadTime("seller")
                    val calendar = java.util.Calendar.getInstance()
                    val currentYear = calendar.get(java.util.Calendar.YEAR)
                    val currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)

                    orders.forEach { order ->
                        calendar.timeInMillis = order.timestamp
                        val isToday = calendar.get(java.util.Calendar.YEAR) == currentYear && 
                                      calendar.get(java.util.Calendar.DAY_OF_YEAR) == currentDay

                        if (isToday && (order.status == "Menunggu Validasi" || order.status == "Belum Bayar") && !com.example.antriin.utils.NotificationState.notifiedOrderIds.contains(order.orderId)) {
                            com.example.antriin.utils.NotificationState.notifiedOrderIds.add(order.orderId)
                            if (!isInitialLoad || order.lastUpdated > lastReadTime) {
                                val menuNames = order.items.formatMenuNames()
                                com.example.antriin.utils.NotificationHelper.showSellerNewOrderNotification(context, order.buyerName, menuNames, order.paymentMethod)
                            }
                        }
                    }
                    isInitialLoad = false
                }
            }
        }
    }
}
