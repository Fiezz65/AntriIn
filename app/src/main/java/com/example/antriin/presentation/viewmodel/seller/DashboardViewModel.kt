package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.Order
import com.example.antriin.domain.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {

    private val _incomingOrders = MutableStateFlow<List<Order>>(emptyList())
    val incomingOrders: StateFlow<List<Order>> = _incomingOrders

    private val _portionsSold = MutableStateFlow(842)
    val portionsSold: StateFlow<Int> = _portionsSold

    private val _totalRevenue = MutableStateFlow(12650000)
    val totalRevenue: StateFlow<Int> = _totalRevenue

    init {
        loadDummyOrders()
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
                    OrderItem(menuName = "Nasi Goreng Spesial", quantity = 2, price = 40000, notes = "Yang satu pedes, yang satu jangan pake kecap"),
                    OrderItem(menuName = "Es Teh Manis", quantity = 1, price = 5000, notes = "")
                )
            ),
            Order(
                orderId = "#ANT-002",
                buyerName = "Siti Maimunah",
                paymentMethod = "QRIS",
                totalPrice = 18000,
                status = "Menunggu Validasi",
                items = listOf(
                    OrderItem(menuName = "Mie Ayam Jamur", quantity = 1, price = 18000, notes = "Jangan pakai daun bawang ya bang")
                )
            ),
            Order(
                orderId = "#ANT-003",
                buyerName = "Andi K.",
                paymentMethod = "QRIS",
                totalPrice = 15000,
                status = "Diproses",
                items = listOf(
                    OrderItem(menuName = "Mie Ayam Spesial", quantity = 1, price = 15000, notes = "Mienya agak lembek ya")
                )
            ),
            Order(
                orderId = "#ANT-004",
                buyerName = "Citra W.",
                paymentMethod = "Tunai",
                totalPrice = 12000,
                status = "Siap Diambil",
                items = listOf(
                    OrderItem(menuName = "Burger Kampung", quantity = 1, price = 12000, notes = "")
                )
            )
        )
    }
}
