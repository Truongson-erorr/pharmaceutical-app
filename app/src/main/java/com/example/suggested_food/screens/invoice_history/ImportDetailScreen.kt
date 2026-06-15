package com.example.suggested_food.screens.invoice_history

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    val users by viewModel.users.collectAsState()

    val currency = NumberFormat.getInstance(Locale("vi", "VN"))
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    LaunchedEffect(receiptId) {
        viewModel.loadImportReceipt(receiptId)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF2563EB),
                                Color(0xFF38BDF8)
                            )
                        )
                    )
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {
                        Text(
                            "Chi tiết hóa đơn",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    actions = {

                        TextButton(
                            onClick = {
                                receipt?.let { data ->
                                    ImportReceiptImageExporter.export(context, data, userName = users[data.user] ?: data.user)
                                    showDialog = true
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
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
                                        Text("OK", color = Color(0xFF2563EB))
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

                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow("Mã hóa đơn: ", "#${data.id}")
                    InvoiceRow(
                        "Người nhập: ",
                        users[data.user] ?: data.user
                    )

                    InvoiceRow("Ngày nhập: ", formatter.format(Date(data.date)))
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow2Col(
                        "Sản phẩm: ", data.productName,
                        "Số lượng: ", data.quantity.toString(),
                    )

                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow2Col(
                        "Hạn sử dụng: ", data.expiryDate,
                        "Nhà cung cấp: ", data.supplier
                    )

                    if (data.note.isNotEmpty()) {
                        InvoiceRow("Ghi chú: ", data.note)
                    }
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow(
                        "Giá nhập: ",
                        "${currency.format(data.price)} đ"
                    )

                    InvoiceRow(
                        "Tổng tiền: ",
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
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value.ifEmpty { "-" },
            color = Color.Black,
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