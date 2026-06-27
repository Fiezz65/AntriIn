package com.example.antriin.domain.repository

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createOrder(order: com.example.antriin.domain.model.Order): Result<Boolean>
    fun listenForNewOrders(sellerId: String): Flow<com.example.antriin.domain.model.Order>
}
