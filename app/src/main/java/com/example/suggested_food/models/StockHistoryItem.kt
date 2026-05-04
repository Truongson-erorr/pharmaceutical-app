package com.example.suggested_food.models

data class StockHistoryItem(
    val id: String = "",
    val type: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val totalPrice: Int = 0,
    val date: Long = 0L
)