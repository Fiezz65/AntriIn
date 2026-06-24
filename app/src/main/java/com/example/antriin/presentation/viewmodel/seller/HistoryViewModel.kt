package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.Order
import com.example.antriin.domain.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel : ViewModel() {

    private val _completedOrders = MutableStateFlow<List<Order>>(emptyList())
    val completedOrders: StateFlow<List<Order>> = _completedOrders

    init {
        loadDummyHistory()
    }

    private fun loadDummyHistory() {
        _completedOrders.value = listOf(
            Order(
                orderId = "ORD-0042",
                buyerName = "Budi Santoso",
                totalPrice = 45000,
                status = "Selesai",
                items = listOf(
                    OrderItem(menuName = "Nasi Goreng Spesial", quantity = 2, price = 20000, notes = "Satu pedas, satu sedang ya bang"),
                    OrderItem(menuName = "Es Teh Manis", quantity = 1, price = 5000, notes = "Esnya dibanyakin")
                )
            ),
            Order(
                orderId = "ORD-0041",
                buyerName = "Ayu Lestari",
                totalPrice = 24000,
                status = "Selesai",
                items = listOf(
                    OrderItem(menuName = "Mie Ayam Jamur", quantity = 1, price = 18000, notes = "Jangan pakai seledri"),
                    OrderItem(menuName = "Es Jeruk", quantity = 1, price = 6000, notes = "")
                )
            )
        )
    }
}
