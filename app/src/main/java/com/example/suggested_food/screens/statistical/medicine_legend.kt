package com.example.suggested_food.screens.statistical

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ImportLegend(
    data: Map<String, Int>
) {
    val colors = listOf(
        Color(0xFF2563EB),
        Color(0xFF38BDF8),
        Color(0xFF10B981),
        Color(0xFFF59E0B),
        Color(0xFFEF4444)
    )
    val total = data.values.sum()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E7EB)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            data.entries.forEachIndexed { index, item ->
                val percent =
                    item.value * 100f / total

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    colors[index % colors.size],
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = item.key,
                            color = Color(0xFF374151)
                        )
                    }

                    Text(
                        text = String.format("%.1f%%", percent),
                        color = Color(0xFF6B7280)
                    )
                }

                if (index != data.size - 1) {
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB)
                    )
                }
            }
        }
    }
}