package com.example.suggested_food.screens.activitylog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.suggested_food.models.ActivityLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityLogDetailDialog(
    log: ActivityLog,
    userName: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (log.type == "IMPORT")
                            "CHI TIẾT NHẬP KHO"
                        else
                            "CHI TIẾT XUẤT KHO",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Thời gian thực hiện: ${formatDate(log.timestamp)}",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                DetailRow(
                    label = "Sản phẩm",
                    value = log.productName
                )

                DetailRow(
                    label = "Số lượng",
                    value = log.quantity.toString()
                )

                DetailRow(
                    label = "Người thực hiện",
                    value = userName
                )

                DetailRow(
                    label = "Loại",
                    value = if (log.type == "IMPORT")
                        "Nhập kho"
                    else
                        "Xuất kho"
                )

                DetailRow(
                    label = "Nội dung",
                    value = log.message
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    )
                ) {
                    Text(
                        text = "Đóng",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Text(
                text = value,
                modifier = Modifier.weight(2f),
                color = Color.Black,
                fontSize = 14.sp
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFF1F5F9)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )
    }
}

private fun formatDate(
    timestamp: Long
): String {

    return SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(Date(timestamp))
}