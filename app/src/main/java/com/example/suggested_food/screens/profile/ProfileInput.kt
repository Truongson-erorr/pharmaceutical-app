package com.example.suggested_food.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val blue = Color(0xFF38BDF8)
val deepBlue = Color(0xFF2563EB)

@Composable
fun ProfileInput(
    label: String,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (enabled) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE5E7EB),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFE5E7EB),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}