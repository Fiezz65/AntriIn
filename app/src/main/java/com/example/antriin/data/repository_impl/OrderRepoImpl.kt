package com.example.antriin.data.repository_impl

import com.example.antriin.domain.model.Order
import com.example.antriin.domain.repository.OrderRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class OrderRepoImpl(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : OrderRepository {
    override suspend fun createOrder(order: Order): Result<Boolean> {
        return try {
            val ref = database.getReference("Orders")
            val newOrderRef = ref.push()
            val orderId = newOrderRef.key ?: return Result.failure(Exception("Failed to generate order ID"))
            
            val orderToSave = order.copy(orderId = orderId)
            newOrderRef.setValue(orderToSave).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
