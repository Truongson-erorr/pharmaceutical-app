package com.example.suggested_food.models

data class ImportReceipt(
    val id: String = "",
    val productId: String = "",
    val date: Long = 0L,
    val user: String = "",
    val productName: String = "",
    val unit: String = "",
    val quantity: Int = 0,
    val price: Int = 0,
    val lot: String = "",
    val expiryDate: String = "",
    val supplier: String = "",
    val note: String = "",
    val totalPrice: Int = 0
)