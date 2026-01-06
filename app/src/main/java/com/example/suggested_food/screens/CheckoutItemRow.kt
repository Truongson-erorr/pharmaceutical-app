package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.suggested_food.models.CartItemModel

@Composable
fun CheckoutItemRow(item: CartItemModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.image,
            contentDescription = item.name,
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = Color(0xFFF2F2F2),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Số lượng: ${item.quantity}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = formatVND(item.price * item.quantity),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B0000)
        )
    }
}
