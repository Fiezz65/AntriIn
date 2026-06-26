package com.example.antriin.data.repository_impl

import com.example.antriin.data.local.CartDao
import com.example.antriin.domain.model.CartItem
import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepoImpl(private val cartDao: CartDao) : CartRepository {

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    override suspend fun addToCart(menu: Menu, quantity: Int, notes: String) {
        val existingItem = cartDao.getCartItemByMenuId(menu.id)
        if (existingItem != null) {
            val updated = existingItem.copy(
                quantity = existingItem.quantity + quantity,
                notes = notes
            )
            cartDao.updateCartItem(updated)
        } else {
            val newItem = CartItem(
                menuId = menu.id,
                menuName = menu.name,
                canteenName = menu.canteenName,
                price = menu.price,
                quantity = quantity,
                notes = notes
            )
            cartDao.insertCartItem(newItem)
        }
    }

    override suspend fun updateQuantity(menuId: String, newQty: Int) {
        val existingItem = cartDao.getCartItemByMenuId(menuId)
        if (existingItem != null) {
            if (newQty <= 0) {
                cartDao.deleteCartItem(existingItem)
            } else {
                val updated = existingItem.copy(quantity = newQty)
                cartDao.updateCartItem(updated)
            }
        }
    }

    override suspend fun clearCart() {
        cartDao.deleteAll()
    }
}
