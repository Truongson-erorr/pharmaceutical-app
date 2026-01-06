package com.example.suggested_food.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PaymentMethodSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    Column {
        Text("Phương thức thanh toán", fontWeight = FontWeight.Bold)

        ShippingOption(
            text = "Thanh toán khi nhận hàng (COD)",
            selected = selected == "COD"
        ) { onSelect("COD") }

        ShippingOption(
            text = "Chuyển khoản VietQR",
            selected = selected == "VIETQR"
        ) { onSelect("VIETQR") }
    }
}

@Composable
fun ShippingOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}