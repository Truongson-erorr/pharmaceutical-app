package com.example.suggested_food.screens.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.suggested_food.screens.home.formatVND

@Composable
fun PaymentSummary(
    subtotal: Double,
    shippingFee: Double,
    total: Double
) {
    Column {
        Text("Chi tiết thanh toán", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        SummaryRow("Tạm tính", formatVND(subtotal))
        SummaryRow("Phí vận chuyển", formatVND(shippingFee))

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        SummaryRow(
            "Tổng cộng",
            formatVND(total),
            isBold = true,
            highlight = true
        )
    }
}