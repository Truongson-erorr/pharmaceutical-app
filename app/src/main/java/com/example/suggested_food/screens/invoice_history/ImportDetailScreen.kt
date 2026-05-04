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
import com.example.suggested_food.viewmodel.ImportViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ImportDetailScreen(
    receiptId: String,
    viewModel: ImportViewModel
) {

    val receipt by viewModel.selectedReceipt.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(receiptId) {
        viewModel.loadImportReceipt(receiptId)
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
            "Chi tiết hóa đơn nhập",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        DetailItem("ID hóa đơn", data.id)
        DetailItem("Người nhập", data.user)
        DetailItem("Tên sản phẩm", data.productName)
        DetailItem("Đơn vị", data.unit)
        DetailItem("Số lượng", data.quantity.toString())
        DetailItem("Giá nhập", "${currency.format(data.price)} đ")
        DetailItem("Tổng tiền", "${currency.format(data.totalPrice)} đ")
        DetailItem("Lô hàng", data.lot)
        DetailItem("Nhà cung cấp", data.supplier)
        DetailItem("Hạn sử dụng", data.expiryDate)
        DetailItem(
            "Ngày nhập",
            formatter.format(Date(data.date))
        )
        DetailItem("Ghi chú", data.note)
        DetailItem("Product ID", data.productId)
    }
}

@Composable
fun DetailItem(
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