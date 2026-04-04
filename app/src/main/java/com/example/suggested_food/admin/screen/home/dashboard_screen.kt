package com.example.suggested_food.admin.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val primaryColor = Color(0xFF08A045)
private val secondaryColor = Color(0xFFB2FF59)
private val cardBackground = Color.White

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
            .padding(16.dp)
    ) {
        Text(
            "Thống kê đơn hàng tuần",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        OrdersChart()
    }
}

@Composable
fun OrdersChart() {
    val data = listOf(5, 8, 3, 12, 6, 10, 7)
    val maxValue = (data.maxOrNull() ?: 1).toFloat()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { value ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(value / maxValue)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(primaryColor, secondaryColor)
                        ),
                        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                    )
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
            Text(day, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}