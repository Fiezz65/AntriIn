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
        _totalPrice.value = items.sumOf { it.price * it.quantity }
    }

    fun addToCart(menuId: String, name: String, canteen: String, price: Int, quantity: Int, notes: String) {
        val currentList = _cartItems.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.menuId == menuId && it.notes == notes }

        if (existingIndex != -1) {
            val existingItem = currentList[existingIndex]
            currentList[existingIndex] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            currentList.add(
                CartItem(
                    id = (currentList.size + 1),
                    menuId = menuId,
                    menuName = name,
                    canteenName = canteen,
                    price = price,
                    quantity = quantity,
                    notes = notes
                )
            )
        }
        _cartItems.value = currentList
        calculateTotal(currentList)
    }

    fun removeFromCart(itemId: Int) {
        val currentList = _cartItems.value.toMutableList()
        currentList.removeIf { it.id == itemId }
        _cartItems.value = currentList
        calculateTotal(currentList)
    }

    fun updateQuantity(itemId: Int, delta: Int) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == itemId }
        if (index != -1) {
            val newItem = currentList[index].copy(quantity = (currentList[index].quantity + delta).coerceAtLeast(1))
            currentList[index] = newItem
            _cartItems.value = currentList
            calculateTotal(currentList)
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0
    }
}