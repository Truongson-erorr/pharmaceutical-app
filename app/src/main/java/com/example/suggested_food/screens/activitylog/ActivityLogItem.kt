package com.example.suggested_food.screens.activitylog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suggested_food.models.ActivityLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityLogItem(
    log: ActivityLog,
    userName: String
) {
    val date = remember(log.timestamp) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(log.timestamp))
    }

    val actionColor = when (log.type) {
        "IMPORT" -> Color(0xFF22C55E)
        "EXPORT" -> Color(0xFFEF4444)
        "PRODUCT_ADD" -> Color(0xFF3B82F6)
        "PRODUCT_UPDATE" -> Color(0xFFF59E0B)
        "PRODUCT_DELETE" -> Color(0xFF6B7280)
        else -> Color(0xFF9CA3AF)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(50))
                            .background(actionColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = log.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = log.message,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Thực hiện bởi: $userName",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = date,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Divider(
            color = Color(0xFFCBD5E1),
            thickness = 0.8.dp
        )
    }
}