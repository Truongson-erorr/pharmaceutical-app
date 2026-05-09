package com.example.suggested_food.screens.invoice_history

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodel.ImportViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportDetailScreen(
    navController: NavController,
    receiptId: String,
    viewModel: ImportViewModel
) {
    val receipt by viewModel.selectedReceipt.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(receiptId) {
        viewModel.loadImportReceipt(receiptId)
    }

    val currency = NumberFormat.getInstance(Locale("vi", "VN"))
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var showDialog by remember { mutableStateOf(false) }

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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            receipt?.let { data ->
                                ImportReceiptImageExporter.export(context, data)
                                showDialog = true
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF1565C0)
                        )
                    ) {
                        Text(
                            "Xuất hóa đơn nhập",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("OK")
                                }
                            },
                            title = {
                                Text("Xuất hóa đơn thành công")
                            },
                            containerColor = Color.White
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
                .background(Color(0xFFF5F5F5))
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
                        text = "HÓA ĐƠN NHẬP",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Divider()

                    InvoiceRow("Mã số phiếu", data.id)
                    InvoiceRow("Người nhập", data.user)
                    InvoiceRow(
                        "Ngày",
                        formatter.format(Date(data.date))
                    )
                    Divider()

                    InvoiceRow("Sản phẩm", data.productName)

                    InvoiceRow2Col(
                        "Đơn vị", data.unit,
                        "Số lượng", data.quantity.toString()
                    )
                    Divider()

                    InvoiceRow2Col(
                        "Lô", data.lot,
                        "HSD", data.expiryDate
                    )

                    InvoiceRow("Nhà cung cấp", data.supplier)

                    if (data.note.isNotEmpty()) {
                        InvoiceRow("Ghi chú", data.note)
                    }
                    Divider()

                    InvoiceRow(
                        "Giá nhập",
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

@Composable
fun InvoiceRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray
        )

        Text(
            text = value.ifEmpty { "-" },
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InvoiceRow2Col(
    label1: String,
    value1: String,
    label2: String,
    value2: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        InvoiceRow(label1, value1)
        InvoiceRow(label2, value2)
    }
}