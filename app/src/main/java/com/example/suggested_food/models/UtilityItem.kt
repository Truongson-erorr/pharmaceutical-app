package com.example.suggested_food.models

import androidx.compose.ui.graphics.vector.ImageVector

data class UtilityItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)