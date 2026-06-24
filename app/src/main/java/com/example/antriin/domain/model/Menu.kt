package com.example.antriin.domain.model

import com.google.firebase.database.PropertyName

data class Menu(
    val id: String = "",
    val sellerId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val category: String = "",
    @get:PropertyName("isSoldOut")
    @set:PropertyName("isSoldOut")
    var isSoldOut: Boolean = false,
    val icon: String = "🍽️"
)