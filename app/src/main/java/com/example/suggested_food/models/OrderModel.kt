package com.example.suggested_food.models

data class OrderModel(
    val id: String = "",
    val userId: String = "",
    val createdAt: Long = 0L,
    val status: String = "",
    val paymentMethod: String = "",
    val subtotal: Double = 0.0,
    val shippingFee: Double = 0.0,
    val total: Double = 0.0,
    val items: List<OrderItem> = emptyList()
)

data class OrderItem(
    val productId: String = "",
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0
)

