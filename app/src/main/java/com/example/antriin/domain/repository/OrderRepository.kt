package com.example.antriin.domain.repository

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createOrder(order: com.example.antriin.domain.model.Order): Result<Boolean>
    fun listenForNewOrders(sellerId: String): Flow<com.example.antriin.domain.model.Order>
    fun getSellerOrders(sellerId: String): Flow<List<com.example.antriin.domain.model.Order>>
    fun getStudentOrders(studentId: String): Flow<List<com.example.antriin.domain.model.Order>>
    suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Boolean>
    
    fun getUnreadCount(orders: List<com.example.antriin.domain.model.Order>, role: String): Int
    fun markAsRead(role: String)
}
