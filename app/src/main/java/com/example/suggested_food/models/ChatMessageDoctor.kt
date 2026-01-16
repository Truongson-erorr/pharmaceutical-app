package com.example.suggested_food.models

data class ChatMessageDoctor(
    val senderId: String = "",
    val senderRole: String = "", // USER / DOCTOR
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
