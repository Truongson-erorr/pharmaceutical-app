package com.example.suggested_food.models

data class ReminderEntity(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val triggerTime: Long = 0L,
    val repeatInterval: Int = 0,
    val medicineId: Long? = null,
    val medicineName: String? = null,
    val actionType: String = "CUSTOM",
    val isEnabled: Boolean = true,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)