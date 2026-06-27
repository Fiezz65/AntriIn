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
import android.content.Context

class OrderRepoImpl(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val context: Context
) : OrderRepository {
    private val prefs = context.getSharedPreferences("antriin_prefs", Context.MODE_PRIVATE)
    
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
        
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue(Order::class.java)
                if (order != null && (order.status == "Belum Bayar" || order.status == "Menunggu Validasi")) {
                    trySend(order)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }

        query.addChildEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    override fun getSellerOrders(sellerId: String): Flow<List<Order>> = callbackFlow {
        val ref = database.getReference("Orders")
        val query = ref.orderByChild("sellerId").equalTo(sellerId)

        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (child in snapshot.children) {
                    val order = child.getValue(Order::class.java)
                    if (order != null) {
                        orders.add(order)
                    }
                }
                orders.sortByDescending { it.timestamp }
                trySend(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }

        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    override fun getStudentOrders(studentId: String): Flow<List<Order>> = callbackFlow {
        val ref = database.getReference("Orders")
        val query = ref.orderByChild("buyerId").equalTo(studentId)

        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (child in snapshot.children) {
                    val order = child.getValue(Order::class.java)
                    if (order != null) {
                        orders.add(order)
                    }
                }
                orders.sortByDescending { it.timestamp }
                trySend(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }

        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    override fun getUnreadCount(orders: List<Order>, role: String): Int {
        val lastReadTime = prefs.getLong("last_read_time_$role", 0L)
        return orders.count { it.timestamp > lastReadTime }
    }

    override fun markAsRead(role: String) {
        prefs.edit().putLong("last_read_time_$role", System.currentTimeMillis()).apply()
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Boolean> {
        return try {
            val ref = database.getReference("Orders").child(orderId)
            ref.child("status").setValue(newStatus).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
