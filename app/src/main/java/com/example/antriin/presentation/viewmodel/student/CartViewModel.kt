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
import com.example.antriin.domain.repository.OrderRepository
import com.example.antriin.domain.repository.UserRepository
import com.example.antriin.domain.model.Order
import com.example.antriin.domain.model.OrderItem
import com.example.antriin.domain.model.OrderStatus
import kotlinx.coroutines.flow.combine

class CartViewModel(
    private val cartRepository: CartRepository,
    private val menuRepository: MenuRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice

    private val _checkoutSuccess = MutableStateFlow(false)
    val checkoutSuccess: StateFlow<Boolean> = _checkoutSuccess

    private var cachedMenus: List<Menu> = emptyList()

    init {
        viewModelScope.launch {
            combine(
                cartRepository.getAllCartItems(),
                menuRepository.getAllMenus()
            ) { cart, menus ->
                Pair(cart, menus)
            }.collectLatest { (cart, menus) ->
                val validCartItems = mutableListOf<CartItem>()
                for (item in cart) {
                    val firebaseMenu = menus.find { it.id == item.menuId }
                    if (firebaseMenu != null && !firebaseMenu.soldOut) {
                        validCartItems.add(item)
                    }
                }
                
                cachedMenus = menus
                
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

    fun checkout(paymentMethod: String) {
        viewModelScope.launch {
            try {
                val currentCart = _cartItems.value
                if (currentCart.isEmpty()) return@launch

                val user = userRepository.getCurrentUser() ?: return@launch
                
                val firstItem = currentCart.first()
                val menu = cachedMenus.find { it.id == firstItem.menuId } ?: return@launch
                val sellerId = menu.sellerId

                val orderItems = currentCart.map { item ->
                    OrderItem(
                        menuId = item.menuId,
                        menuName = item.menuName,
                        quantity = item.quantity,
                        price = item.price,
                        notes = item.notes
                    )
                }

                val order = Order(
                    buyerId = user.uid,
                    buyerName = user.fullName,
                    sellerId = sellerId,
                    items = orderItems,
                    totalPrice = _totalPrice.value,
                    paymentMethod = paymentMethod,
                    status = OrderStatus.WAITING_VALIDATION,
                    timestamp = System.currentTimeMillis()
                )

                val result = orderRepository.createOrder(order)
                if (result.isSuccess) {
                    cartRepository.clearCart()
                    _checkoutSuccess.value = true
                }
            } catch (e: Exception) {
            }
        }
    }

    fun resetCheckoutStatus() {
        _checkoutSuccess.value = false
    }

    fun getMenuIcon(menuId: String): String {
        return cachedMenus.find { it.id == menuId }?.icon ?: "🍱"
    }
}
