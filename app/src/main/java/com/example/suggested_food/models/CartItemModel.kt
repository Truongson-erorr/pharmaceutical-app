package com.example.suggested_food.models

data class CartItemModel(
    val productId: String = "",
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)