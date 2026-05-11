package com.example.suggested_food.models

data class Patient(
    val id: String = "",
    val name: String = "",
    val phone: String = "",

    val address: String = "",
    val gender: String = "",
    val birthYear: Int = 0,

    val totalOrders: Int = 0,
    val totalSpent: Long = 0L,

    val lastVisit: Long = 0L,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)