package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LiveTrackingViewModel : ViewModel() {

    private val _queueList = MutableStateFlow<List<Order>>(emptyList())
    val queueList: StateFlow<List<Order>> = _queueList

    init {
        loadDummyQueue()
    }

    private fun loadDummyQueue() {
        _queueList.value = listOf(
            Order(orderId = "#ANT-090", buyerName = "Budi S.", status = "Siap Diambil"),
            Order(orderId = "#ANT-091", buyerName = "Andi K.", status = "Diproses"),
            Order(orderId = "#ANT-092", buyerName = "Saya", status = "Diproses"),
            Order(orderId = "#ANT-093", buyerName = "Citra W.", status = "Menunggu Validasi")
        )
    }
}
