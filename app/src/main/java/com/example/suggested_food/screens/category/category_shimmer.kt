package com.example.suggested_food.screens.category

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun CategoryShimmerItem() {
    val shimmerColor = Color(0xFFC7CBD1)

    Surface(
        modifier = Modifier
            .size(120.dp)
            .shimmer(),
        shape = RoundedCornerShape(14.dp),
        color = shimmerColor,
        shadowElevation = 1.dp
    ) {}
}