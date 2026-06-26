package com.example.antriin.presentation.viewmodel.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.CartItem
import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import com.example.antriin.domain.repository.MenuRepository
import kotlinx.coroutines.flow.combine

class CartViewModel(
    private val cartRepository: CartRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice

    init {
        viewModelScope.launch {
            combine(
                cartRepository.getAllCartItems(),
                menuRepository.getAllMenus()
            ) { cart, menus ->
                Pair(cart, menus)
            }.collectLatest { (cart, menus) ->
                // Check if any cart item is now sold out or removed from firebase
                val validCartItems = mutableListOf<CartItem>()
                for (item in cart) {
                    val firebaseMenu = menus.find { it.id == item.menuId }
                    if (firebaseMenu == null || firebaseMenu.soldOut) {
                        // Automatically remove from cart if sold out or deleted in firebase
                        cartRepository.updateQuantity(item.menuId, 0)
                    } else {
                        validCartItems.add(item)
                    }
                }
                
                _cartItems.value = validCartItems
                calculateTotal(validCartItems)
            }
        }
    }

    fun addToCart(menu: Menu, quantity: Int, notes: String) {
        viewModelScope.launch {
            cartRepository.addToCart(menu, quantity, notes)
        }
    }

    fun updateQuantity(menuId: String, newQty: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(menuId, newQty)
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
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
