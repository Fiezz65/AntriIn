package com.example.antriin.presentation.viewmodel.student

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
    }

    fun addToCart(menu: com.example.antriin.domain.model.Menu, quantity: Int, notes: String) {
        val currentItems = _cartItems.value.toMutableList()
        val existingIndex = currentItems.indexOfFirst { it.menuId == menu.id }
        if (existingIndex != -1) {
            val existing = currentItems[existingIndex]
            currentItems[existingIndex] = existing.copy(quantity = existing.quantity + quantity, notes = notes)
        } else {
            val newItem = CartItem(
                menuId = menu.id,
                menuName = menu.name,
                canteenName = menu.canteenName,
                price = menu.price,
                quantity = quantity,
                notes = notes
            )
            currentItems.add(newItem)
        }
        _cartItems.value = currentItems
        calculateTotal(currentItems)
    }

    fun updateQuantity(menuId: String, newQty: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.menuId == menuId }
        if (index != -1) {
            if (newQty <= 0) {
                currentItems.removeAt(index)
            } else {
                currentItems[index] = currentItems[index].copy(quantity = newQty)
            }
            _cartItems.value = currentItems
            calculateTotal(currentItems)
        }
    }

    private fun calculateTotal(items: List<CartItem>) {
        var total = 0
        for (item in items) {
            total += (item.price * item.quantity)
        }
        _totalPrice.value = total
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0
    }
}
