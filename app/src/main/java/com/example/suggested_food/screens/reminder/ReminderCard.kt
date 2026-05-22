package com.example.suggested_food.screens.reminder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suggested_food.models.ReminderEntity
import com.example.suggested_food.screens.notifications.formatDate
import java.time.LocalDate

@Composable
fun ReminderCard(
    reminder: ReminderEntity,
    isMenuOpen: Boolean,
    onMenuClick: () -> Unit,
    onDoneClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismissMenu: () -> Unit
) {
    val now = System.currentTimeMillis()
    val diff = ((reminder.triggerTime - now) / (1000 * 60 * 60 * 24)).toInt()

    val timeText = when {
        diff > 0 -> "Còn $diff ngày"
        diff < 0 -> "Trễ ${-diff} ngày"
        else -> "Hôm nay"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {

        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = Color(0xFF2563EB)
                    )
                    Spacer(Modifier.width(10.dp))

                    Column {
                        Text(
                            reminder.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )

                        Text(
                            reminder.description ?: "Không có mô tả",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = "Hạn: ${formatDate(reminder.triggerTime)}",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(10.dp))

            Text(
                text = buildString {
                    append(timeText)
                    append(" • ")
                    append(
                        when (reminder.actionType) {
                            "STOCK_IN" -> "Nhập"
                            "STOCK_OUT" -> "Xuất"
                            else -> "Khác"
                        }
                    )
                    reminder.medicineName?.let {
                        append(" • $it")
                    }
                },
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(10.dp))

            val done = reminder.isDone

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (done) Color(0xFFEFFAF3) else Color(0xFFFFF1F2))
                    .clickable(enabled = !done) { onDoneClick() }
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (done) "Đã hoàn thành" else "Chưa hoàn thành",
                    color = if (done) Color(0xFF16A34A) else Color(0xFFDC2626),
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun SimpleDialog(
    title: String,
    text: String,
    confirmText: String,
    confirmColor: Color = Color(0xFF2563EB),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText, color = confirmColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {

    val today = LocalDate.now()

    val days = (-15..15).map {
        today.plusDays(it.toLong())
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(days) { date ->

            val selected = date == selectedDate

            Column(
                modifier = Modifier
                    .width(78.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selected)
                            Color.Black
                        else
                            Color.White
                    )
                    .clickable {
                        onDateSelected(date)
                    }
                    .padding(
                        horizontal = 14.dp,
                        vertical = 14.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = when (date.dayOfWeek.value) {
                        1 -> "Thứ 2"
                        2 -> "Thứ 3"
                        3 -> "Thứ 4"
                        4 -> "Thứ 5"
                        5 -> "Thứ 6"
                        6 -> "Thứ 7"
                        else -> "Chủ nhật"
                    },
                    fontSize = 11.sp,
                    color = if (selected) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${date.dayOfMonth}/${date.monthValue}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (selected) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEF4444), RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}