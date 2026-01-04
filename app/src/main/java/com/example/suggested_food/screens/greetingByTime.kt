package com.example.suggested_food.screens

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun greetingByTime(): String {
    val hour = java.time.LocalTime.now().hour
    return when (hour) {
        in 5..10 -> "Chào buổi sáng ☀️"
        in 11..13 -> "Chào buổi trưa 🌤️"
        in 14..17 -> "Chào buổi chiều 🌇"
        else -> "Chào buổi tối 🌙"
    }
}
