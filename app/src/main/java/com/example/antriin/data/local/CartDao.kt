package com.example.antriin.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.antriin.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertCartItem(item: CartItem): Long

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Delete
    @JvmSuppressWildcards
    suspend fun deleteCartItem(item: CartItem): Int

    @Query("DELETE FROM cart_table")
    @JvmSuppressWildcards
    suspend fun deleteAll(): Int
}