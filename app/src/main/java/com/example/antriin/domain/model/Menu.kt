package com.example.antriin.domain.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class Menu(
    val id: String = "",
    val sellerId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val category: String = "",
    var soldOut: Boolean = false,
    val icon: String = "🍽️",
    val canteenName: String = ""
) {
    @get:Exclude
    @set:Exclude
    var isCanteenOpen: Boolean = true
}