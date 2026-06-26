package com.example.antriin.domain.repository

interface OrderRepository {
    suspend fun createOrder(order: com.example.antriin.domain.model.Order): Result<Boolean>
}
