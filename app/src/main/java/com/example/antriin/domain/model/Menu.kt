package com.example.antriin.domain.model

data class Menu(
    val id: String = "",
    val sellerId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val category: String = "",
    val isSoldOut: Boolean = false
)