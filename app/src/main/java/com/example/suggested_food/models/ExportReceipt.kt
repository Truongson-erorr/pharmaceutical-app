package com.example.suggested_food.models

data class ExportReceipt(
    val id: String = "",
    val date: Long = 0L,
    val user: String = "",

    val productId: String = "",
    val productName: String = "",

    val quantity: Int = 0,
    val price: Int = 0,

    val lot: String = "",
    val expiryDate: String = "",

    val customer: String = "",
    val customerPhone: String = "",
    val totalPrice: Int = 0
)