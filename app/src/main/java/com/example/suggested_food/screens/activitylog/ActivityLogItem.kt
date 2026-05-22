package com.example.suggested_food.screens.activitylog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale.getDefault())
            .format(Date(log.timestamp))
    }

    val style = remember(log.type) {
        getStyle(log.type)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = log.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = log.message,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = style.bg
                ) {
                    Text(
                        text = style.text,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = style.color
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Divider(
                color = Color(0xFFE5E7EB),
                thickness = 0.8.dp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Thực hiện bởi: $userName",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                Text(
                    text = date,
                    fontSize = 13.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}

data class TypeStyle(
    val text: String,
    val color: Color,
    val bg: Color
)

fun getStyle(type: String): TypeStyle {
    return when (type) {

        "PRODUCT_ADD" ->
            TypeStyle("THÊM THUỐC", Color(0xFF16A34A), Color(0xFFE8F5E9))

        "PRODUCT_UPDATE" ->
            TypeStyle("CẬP NHẬT THUỐC", Color(0xFF2563EB), Color(0xFFE3F2FD))

        "PRODUCT_DELETE" ->
            TypeStyle("XÓA THUỐC", Color(0xFFDC2626), Color(0xFFFFEBEE))

        "EXPORT" ->
            TypeStyle("XUẤT THUỐC", Color(0xFFF59E0B), Color(0xFFFFF3E0))

        "IMPORT" ->
            TypeStyle("NHẬP THUỐC", Color(0xFF7C3AED), Color(0xFFF3E5F5))

        else ->
            TypeStyle(type, Color.Gray, Color(0xFFF5F5F5))
    }
}