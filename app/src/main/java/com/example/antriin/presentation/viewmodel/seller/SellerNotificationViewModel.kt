package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SellerNotificationViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val notifications: StateFlow<List<Pair<String, String>>> = _notifications
    
    val unreadCount: StateFlow<Int> = com.example.antriin.utils.NotificationState.sellerUnreadCount

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getSellerOrders(user.uid).collectLatest { orders ->
                    val notifList = mutableListOf<Pair<String, String>>()
                    orders.forEach { order ->
                        val itemCount = order.items.sumOf { it.quantity }
                        when (order.status) {
                            "Menunggu Validasi" -> notifList.add(Pair("Pesanan Baru!", "Pesanan baru dari ${order.buyerName} ($itemCount item, ${order.paymentMethod}) menunggu validasi."))
                            "Belum Bayar" -> notifList.add(Pair("Menunggu Pembayaran", "Pesanan dari ${order.buyerName} ($itemCount item, ${order.paymentMethod}) belum dibayar."))
                        }
                    }
                    _notifications.value = notifList.take(20)
                    com.example.antriin.utils.NotificationState.sellerUnreadCount.value = orderRepository.getUnreadCount(orders, "seller")
                }
            }
        }
    }
    
    fun markAsRead() {
        orderRepository.markAsRead("seller")
        com.example.antriin.utils.NotificationState.sellerUnreadCount.value = 0
    }
    
    private var isListenerStarted = false

    fun startGlobalListener(context: android.content.Context) {
        if (isListenerStarted) return
        isListenerStarted = true
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getSellerOrders(user.uid).collectLatest { orders ->
                    orders.forEach { order ->
                        if ((order.status == "Menunggu Validasi" || order.status == "Belum Bayar") && !com.example.antriin.utils.NotificationState.notifiedOrderIds.contains(order.orderId)) {
                            com.example.antriin.utils.NotificationState.notifiedOrderIds.add(order.orderId)
                            com.example.antriin.utils.NotificationHelper.showSellerNewOrderNotification(context, order.buyerName)
                        }
                    }
                }
            }
        }
    }
}
