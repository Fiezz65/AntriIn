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

    private val _portionsSold = MutableStateFlow(0)
    val portionsSold: StateFlow<Int> = _portionsSold

    private val _totalRevenue = MutableStateFlow(0)
    val totalRevenue: StateFlow<Int> = _totalRevenue

    private var fetchJob: kotlinx.coroutines.Job? = null

    init {
        refresh()
    }

    fun refresh() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null && user.role.equals("penjual", ignoreCase = true)) {
                orderRepository.getSellerOrders(user.uid).collectLatest { orders ->
                    val calendar = java.util.Calendar.getInstance()
                    val currentYear = calendar.get(java.util.Calendar.YEAR)
                    val currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)

                    _incomingOrders.value = orders.filter { order ->
                        val isValidStatus = order.status == "Belum Bayar" || order.status == "Menunggu Validasi" || order.status == "Diproses" || order.status == "Siap Diambil"
                        calendar.timeInMillis = order.timestamp
                        val orderYear = calendar.get(java.util.Calendar.YEAR)
                        val orderDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)
                        isValidStatus && orderYear == currentYear && orderDay == currentDay
                    }

                    val completedToday = orders.filter { order ->
                        calendar.timeInMillis = order.timestamp
                        val orderYear = calendar.get(java.util.Calendar.YEAR)
                        val orderDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)
                        orderYear == currentYear && orderDay == currentDay && order.status == "Selesai"
                    }

                    var income = 0
                    var portions = 0
                    for (order in completedToday) {
                        income += order.totalPrice
                        for (item in order.items) {
                            portions += item.quantity
                        }
                    }
                    _totalRevenue.value = income
                    _portionsSold.value = portions
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
