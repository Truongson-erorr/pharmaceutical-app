package com.example.suggested_food.models

data class ProductData(
    val name: String,             // Medicine Name
    val composition: String,      // Composition
    val uses: String,             // Uses
    val sideEffects: String,      // Side_effects
    val imageUrl: String,         // Image URL
    val manufacturer: String,     // Manufacturer
    val excellentReview: String,  // Excellent Review %
    val averageReview: String,    // Average Review %
    val poorReview: String        // Poor Review %
)