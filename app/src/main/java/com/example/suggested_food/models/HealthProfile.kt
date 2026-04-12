package com.example.suggested_food.models

data class HealthProfile(
    val userId: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val bloodType: String = "",
    val medicalHistory: List<String> = emptyList(),
    val drugAllergies: List<String> = emptyList(),
    val currentMedications: List<String> = emptyList()
)