package com.example.antriin.data.repository_impl

import com.example.antriin.domain.model.User
import com.example.antriin.domain.repository.UserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepoImpl : UserRepository {
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")

    override fun getSellersByLocation(location: String): Flow<List<User>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sellers = mutableListOf<User>()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    if (user != null && user.role == "Penjual" && user.location == location) {
                        sellers.add(user)
                    }
                }
                trySend(sellers)
            }

            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }
        usersRef.addValueEventListener(listener)
        awaitClose { usersRef.removeEventListener(listener) }
    }
}
