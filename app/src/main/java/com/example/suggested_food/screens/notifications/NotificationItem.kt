package com.example.suggested_food.screens.notifications

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suggested_food.models.AppNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationItem(
    item: AppNotification,
    isExpanded: Boolean,
    onToggle: () -> Unit
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
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded)
                Color.White
            else
                Color.White
        ),
        border = if (isExpanded)
            BorderStroke(
                1.dp,
                Color(0xFF9CA3AF)
            )
        else null,
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp)
        ) {

            Row(
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
                    Spacer(Modifier.height(8.dp))

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
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.rotate(if (isExpanded) 90f else 0f)
                )
            }

            if (isExpanded) {
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                                Color.White,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Chi tiết thông báo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {

                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(20.dp)
                                .background(
                                    color = color,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = item.message,
                            color = Color.Gray,
                            lineHeight = 22.sp,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = formatDate(item.time),
                            color = Color.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

fun formatDate(time: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(time))
}