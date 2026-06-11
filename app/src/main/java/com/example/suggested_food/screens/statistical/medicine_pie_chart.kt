package com.example.suggested_food.screens.statistical

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ImportPieChart(
    data: Map<String, Int>
) {
    val total = data.values.sum()

    val colors = listOf(
        Color(0xFF2563EB),
        Color(0xFF38BDF8),
        Color(0xFF10B981),
        Color(0xFFF59E0B),
        Color(0xFFEF4444)
    )

    Canvas(
        modifier = Modifier.size(220.dp)
    ) {
        var startAngle = -90f

        data.entries.forEachIndexed { index, entry ->

            val sweepAngle =
                (entry.value.toFloat() / total) * 360f

            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )

            startAngle += sweepAngle
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}