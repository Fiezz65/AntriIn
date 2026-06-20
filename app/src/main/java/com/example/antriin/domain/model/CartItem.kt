package com.example.antriin.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val menuId: String,
    val menuName: String,
    val canteenName: String,
    val price: Int,
    val quantity: Int,
    val notes: String = ""
)