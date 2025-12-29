package com.example.suggested_food.models

data class ProductModel(
    val id: String = "",
    val categoryId: String = "",
    val name: String = "",
    val images: List<String> = emptyList(),
    val description: String = "",
    val price: Double = 0.0,
    val onSale: Boolean = false,
    val stock: Int = 0,
    val manufacturer: String = "",
    val ingredients: String = "",
    val usage: String = "",
    val expiryDate: String = "",
    val rating: Float = 0f,
    val reviews: List<String> = emptyList()
)
