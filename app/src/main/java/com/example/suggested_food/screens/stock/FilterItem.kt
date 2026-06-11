package com.example.suggested_food.screens.stock

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun FilterMenuItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 45.dp)
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = if (selected)
                FontWeight.Bold
            else
                FontWeight.Normal,
            color = if (selected)
                Color(0xFF38BDF8)
            else
                Color.Gray
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = if (selected)
                Color(0xFF38BDF8)
            else
                Color(0xFF9CA3AF),
            modifier = Modifier.size(20.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isExpired(expiryDate: String): Boolean {
    if (expiryDate.isBlank()) return false

    return try {
        LocalDate.parse(expiryDate)
            .isBefore(LocalDate.now())
    } catch (e: Exception) {
        false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isExpiringSoon(expiryDate: String): Boolean {
    if (expiryDate.isBlank()) return false

    return try {
        val expiry = LocalDate.parse(expiryDate)

        val days = ChronoUnit.DAYS.between(
            LocalDate.now(),
            expiry
        )

        days in 0..30

    } catch (e: Exception) {
        false
    }
}