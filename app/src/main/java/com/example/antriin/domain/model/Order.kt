package com.example.antriin.domain.model

data class Order(
    val orderId: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val sellerId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Int = 0,
    val paymentMethod: String = "",
    val status: String = OrderStatus.WAITING_VALIDATION,
    val timestamp: Long = 0L,
    val updatedAt: Long = 0L
) {
    val lastUpdated: Long get() = if (updatedAt > 0) updatedAt else timestamp
}

data class OrderItem(
    val menuId: String = "",
    val menuName: String = "",
    val quantity: Int = 0,
    val price: Int = 0,
    val notes: String = ""
)

object OrderStatus {
    const val WAITING_VALIDATION = "Menunggu Validasi"
    const val PROCESSING = "Diproses"
    const val READY = "Siap Diambil"
    const val COMPLETED = "Selesai"
}