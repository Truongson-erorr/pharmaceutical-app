package com.example.suggested_food.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.suggested_food.models.AppNotification

@Composable
fun NotificationItem(
    item: AppNotification,
    onClick: (AppNotification) -> Unit
) {
    val color = when (item.type) {
        "IMPORT" -> Color(0xFF2196F3)
        "EXPORT" -> Color(0xFF22C55E)
        "WARNING" -> Color(0xFFFF3B30)
        else -> Color(0xFF607D8B)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(item) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = null,
                    tint = color
                )
            }
            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1C1C1C),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = item.message,
                    color = Color(0xFF6B6B6B),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFE8F8EF),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = formatTime(item.time),
                        color = Color(0xFF22C55E),
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(Modifier.width(8.dp))

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFBDBDBD)
            )
        }
    }
}