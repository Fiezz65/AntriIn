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
        loadDummyOrders() // Keep this for now for UI visualization, or replace it if you have full implementation
    }

    fun startListeningForNewOrders(context: Context) {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null && user.role == "penjual") {
                orderRepository.listenForNewOrders(user.uid).collectLatest { newOrder ->
                    // Trigger local notification when a TRULY new order arrives
                    NotificationHelper.showSellerNewOrderNotification(context, newOrder.buyerName)
                    
                    // Add it to the local list (prepend)
                    val currentList = _incomingOrders.value.toMutableList()
                    // Avoid duplicates if any
                    if (currentList.none { it.orderId == newOrder.orderId }) {
                        currentList.add(0, newOrder)
                        _incomingOrders.value = currentList
                    }
                }
            }
        }
    }

    private fun loadDummyOrders() {
        _incomingOrders.value = listOf(
            Order(
                orderId = "#ANT-001",
                buyerName = "Budi Antoro",
                paymentMethod = "Tunai",
                totalPrice = 45000,
                status = "Belum Bayar",
                items = listOf(
                    OrderItem(menuId = "1", menuName = "Nasi Goreng Spesial", quantity = 2, price = 40000, notes = "Yang satu pedes, yang satu jangan pake kecap"),
                    OrderItem(menuId = "2", menuName = "Es Teh Manis", quantity = 1, price = 5000, notes = "")
                )
            )
        )
    }
}
