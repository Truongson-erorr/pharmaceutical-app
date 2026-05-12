package com.example.suggested_food.models

data class AppNotification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val time: Long = System.currentTimeMillis(),
    val type: String = "",
    val isRead: Boolean = false
)