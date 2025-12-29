package com.example.suggested_food.models

data class UserModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
