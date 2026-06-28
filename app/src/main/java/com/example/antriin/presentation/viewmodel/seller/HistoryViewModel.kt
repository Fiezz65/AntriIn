package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.Order
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _completedOrders = MutableStateFlow<List<Order>>(emptyList())
    val completedOrders: StateFlow<List<Order>> = _completedOrders.asStateFlow()

    private var fetchJob: kotlinx.coroutines.Job? = null

    init {
        refresh()
    }

    fun refresh() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                orderRepository.getSellerOrders(user.uid).collect { orders ->
                    val calendar = java.util.Calendar.getInstance()
                    val currentYear = calendar.get(java.util.Calendar.YEAR)
                    val currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)

                    _completedOrders.value = orders.filter { order ->
                        calendar.timeInMillis = order.timestamp
                        val orderYear = calendar.get(java.util.Calendar.YEAR)
                        val orderDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)
                        
                        order.status == "Selesai" && orderYear == currentYear && orderDay == currentDay
                    }.sortedByDescending { it.timestamp }
                }
            }
        }
    }
}
