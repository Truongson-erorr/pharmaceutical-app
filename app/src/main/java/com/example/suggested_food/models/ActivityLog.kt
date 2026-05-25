package com.example.suggested_food.models

data class ActivityLog(
    val id: String = "",
    val type: String = "",
    val title: String = "",
    val message: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val userId: String = "",
    val userName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)