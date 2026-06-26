package com.example.antriin.domain.repository

import com.example.antriin.domain.model.CartItem
import com.example.antriin.domain.model.Menu
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getAllCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(menu: Menu, quantity: Int, notes: String)
    suspend fun updateQuantity(menuId: String, newQty: Int)
    suspend fun clearCart()
}
