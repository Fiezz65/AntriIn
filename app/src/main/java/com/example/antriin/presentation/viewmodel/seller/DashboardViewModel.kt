package com.example.antriin.presentation.viewmodel.seller

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.Order
import com.example.antriin.domain.model.OrderItem
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import com.example.antriin.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _incomingOrders = MutableStateFlow<List<Order>>(emptyList())
    val incomingOrders: StateFlow<List<Order>> = _incomingOrders

    private val _portionsSold = MutableStateFlow(842)
    val portionsSold: StateFlow<Int> = _portionsSold

    private val _totalRevenue = MutableStateFlow(12650000)
    val totalRevenue: StateFlow<Int> = _totalRevenue

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null && user.role.equals("penjual", ignoreCase = true)) {
                orderRepository.getSellerOrders(user.uid).collectLatest { orders ->
                    _incomingOrders.value = orders.filter { 
                        it.status == "Belum Bayar" || it.status == "Menunggu Validasi" || it.status == "Diproses" || it.status == "Siap Diambil" 
                    }
                }
            }
        }
    }

    fun updateOrderStatus(orderId: String, currentStatus: String) {
        viewModelScope.launch {
            val newStatus = when (currentStatus) {
                "Menunggu Validasi", "Belum Bayar" -> "Diproses"
                "Diproses" -> "Siap Diambil"
                "Siap Diambil" -> "Selesai"
                else -> return@launch
            }
            orderRepository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, "Dibatalkan")
        }
    }
}
