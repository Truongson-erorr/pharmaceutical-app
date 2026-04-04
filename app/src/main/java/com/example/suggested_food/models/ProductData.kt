package com.example.suggested_food.models

data class ProductData(
    val id: Int,
    val name: String,
    val composition: String,
    val uses: String,
    val sideEffects: String,
    val imageUrl: String,
    val manufacturer: String,
    val excellentReview: String,
    val averageReview: String,
    val poorReview: String,
)