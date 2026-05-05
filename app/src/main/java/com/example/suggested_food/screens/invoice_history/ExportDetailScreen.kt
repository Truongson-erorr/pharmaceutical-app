package com.example.suggested_food.screens.invoice_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodel.ExportViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportDetailScreen(
    navController: NavController,
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

    Scaffold(
        containerColor = Color.White,

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Text(
                        "Chi tiết hóa đơn",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },

                actions = {
                    TextButton(
                        onClick = {

                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFE65100)
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                Color(0xFFFFE0B2),
                                RoundedCornerShape(50.dp)
                            )
                            .height(33.dp)
                    ) {
                        Text(
                            "Xuất PDF",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (loading || receipt == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val data = receipt!!

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF4F7FB))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "HÓA ĐƠN XUẤT",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(22.dp))
                    Divider()

                    InvoiceRow("Mã số phiếu", data.id)
                    InvoiceRow("Người xuất", data.user)
                    InvoiceRow(
                        "Ngày",
                        formatter.format(Date(data.date))
                    )
                    Divider()

                    InvoiceRow("Sản phẩm", data.productName)

                    InvoiceRow(
                        "Số lượng",
                        data.quantity.toString()
                    )
                    Divider()

                    InvoiceRow2Col(
                        "Lô", data.lot,
                        "HSD", data.expiryDate
                    )

                    InvoiceRow(
                        "Khách hàng",
                        data.customer
                    )
                    Divider()

                    InvoiceRow(
                        "Giá xuất",
                        "${currency.format(data.price)} đ"
                    )

                    InvoiceRow(
                        "Tổng tiền",
                        "${currency.format(data.totalPrice)} đ"
                    )
                }
            }
        }
    }
}