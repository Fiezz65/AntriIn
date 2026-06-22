package com.example.antriin.presentation.student

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice

    init {
        loadDummyCart()
    }

    private fun loadDummyCart() {
        val dummyData = listOf(
            CartItem(id = 1, menuId = "1", menuName = "Nasi Ayam Geprek Level 5", canteenName = "Kantin Teknik Mpok", price = 18000, quantity = 1),
            CartItem(id = 2, menuId = "3", menuName = "Es Teh Manis Jumbo", canteenName = "Kantin Teknik Mpok", price = 5000, quantity = 2)
        )
        _cartItems.value = dummyData
        calculateTotal(dummyData)
    }

    private fun calculateTotal(items: List<CartItem>) {
        var total = 0
        for (item in items) {
            total += (item.price * item.quantity)
        }
        _totalPrice.value = total
    }
}