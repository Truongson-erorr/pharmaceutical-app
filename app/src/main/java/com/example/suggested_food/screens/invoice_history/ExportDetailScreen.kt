package com.example.suggested_food.screens.invoice_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.suggested_food.viewmodel.ExportViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExportDetailScreen(
    receiptId: String,
    viewModel: ExportViewModel
) {

    val receipt by viewModel.selectedReceipt.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(receiptId) {
        viewModel.loadExportReceipt(receiptId)
    }

    val currency =
        NumberFormat.getInstance(Locale("vi", "VN"))

    val formatter =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (loading || receipt == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val data = receipt!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            "Chi tiết hóa đơn xuất",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Item("ID hóa đơn", data.id)
        Item("Người xuất", data.user)
        Item("Tên sản phẩm", data.productName)
        Item("Số lượng", data.quantity.toString())
        Item("Giá xuất", "${currency.format(data.price)} đ")
        Item("Tổng tiền", "${currency.format(data.totalPrice)} đ")
        Item("Khách hàng", data.customer)
        Item("Lô hàng", data.lot)
        Item("Hạn sử dụng", data.expiryDate)
        Item(
            "Ngày xuất",
            formatter.format(Date(data.date))
        )
        DetailItem("Product ID", data.productId)
    }
}

@Composable
fun Item(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = value.ifEmpty { "-" },
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}