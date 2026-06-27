package com.example.antriin.data.repository_impl

import com.example.antriin.domain.model.Order
import com.example.antriin.domain.repository.OrderRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override fun listenForNewOrders(sellerId: String): Flow<Order> = callbackFlow {
        val ref = database.getReference("Orders")
        val query = ref.orderByChild("sellerId").equalTo(sellerId)
        
        // We only want to notify for TRULY new orders.
        // We record the timestamp when we start listening.
        val listenStartTime = System.currentTimeMillis()

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue(Order::class.java)
                if (order != null && order.timestamp > listenStartTime) {
                    trySend(order)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        query.addChildEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }
}
