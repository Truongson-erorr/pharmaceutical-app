package com.example.suggested_food.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddressSection() {
    Column {
        Text("Địa chỉ nhận hàng", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Nguyễn Văn A | 0123 456 789")
        Text("123 Nguyễn Trãi, Quận 1, TP.HCM", color = Color.Gray)
    }
}