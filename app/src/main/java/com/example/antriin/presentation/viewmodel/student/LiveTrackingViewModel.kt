package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.Order
import com.example.antriin.domain.model.OrderStatus
import com.example.antriin.domain.model.User
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LiveTrackingViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder

    private val _queueList = MutableStateFlow<List<Order>>(emptyList())
    val queueList: StateFlow<List<Order>> = _queueList

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _sellerName = MutableStateFlow<String>("")
    val sellerName: StateFlow<String> = _sellerName

    private val _sellers = MutableStateFlow<List<User>>(emptyList())
    val sellers: StateFlow<List<User>> = _sellers

    private val _selectedSellerId = MutableStateFlow<String?>(null)
    val selectedSellerId: StateFlow<String?> = _selectedSellerId

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                _currentUser.value = user
                orderRepository.getStudentOrders(user.uid).collectLatest { orders ->
                    val activeOrder = orders.sortedBy { it.timestamp }.find {
                        it.status != OrderStatus.COMPLETED && it.status != "Dibatalkan"
                    }
                    _currentOrder.value = activeOrder

                    if (activeOrder != null) {
                        listenToSellerQueue(activeOrder.sellerId)
                    } else {
                        viewModelScope.launch {
                            val fetchedSellers = userRepository.getAllSellers()
                            _sellers.value = fetchedSellers
                            if (fetchedSellers.isNotEmpty()) {
                                if (_selectedSellerId.value == null) {
                                    _selectedSellerId.value = fetchedSellers.first().uid
                                }
                                listenToSellerQueue(_selectedSellerId.value!!)
                            } else {
                                _queueList.value = emptyList()
                                _isBusy.value = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun selectSeller(sellerId: String) {
        _selectedSellerId.value = sellerId
        listenToSellerQueue(sellerId)
    }

    private var sellerQueueJob: kotlinx.coroutines.Job? = null

    private fun listenToSellerQueue(sellerId: String) {
        viewModelScope.launch {
            val seller = userRepository.getUserById(sellerId)
            if (seller != null) {
                _sellerName.value = seller.fullName
            }
        }
        
        sellerQueueJob?.cancel()
        sellerQueueJob = viewModelScope.launch {
            orderRepository.getSellerOrders(sellerId).collectLatest { sellerOrders ->
                val activeQueue = sellerOrders.filter {
                    it.status == OrderStatus.WAITING_VALIDATION || it.status == OrderStatus.PROCESSING || it.status == OrderStatus.READY
                }.sortedBy { it.timestamp }
                
                _queueList.value = activeQueue
                _isBusy.value = activeQueue.size > 10
            }
        }
    }
}
