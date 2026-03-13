package com.example.suggested_food.screens.home

import java.text.NumberFormat
import java.util.Locale

fun formatVND(price: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(price)
}
