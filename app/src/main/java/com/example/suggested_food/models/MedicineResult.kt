package com.example.suggested_food.models

data class MedicineResult(
    val name: String,
    val score: Float,
    val composition: String,
    val uses: String,
    val sideEffects: String,
    val imageUrl: String,
    val manufacturer: String,
    val excellent: String,
    val average: String,
    val poor: String
)