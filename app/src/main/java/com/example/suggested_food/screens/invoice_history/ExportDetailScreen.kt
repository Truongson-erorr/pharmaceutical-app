package com.example.suggested_food.screens.invoice_history

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodel.ExportViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

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

    val context = androidx.compose.ui.platform.LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val users by viewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
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
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
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

                                    kotlinx.coroutines.CoroutineScope(
                                        kotlinx.coroutines.Dispatchers.IO
                                    ).launch {

                                        val file =
                                            ExportHistoryImageExporter.export(context, data, userName = users[data.user] ?: data.user)

                                        kotlinx.coroutines.withContext(
                                            kotlinx.coroutines.Dispatchers.Main
                                        ) {
                                            showDialog = true
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            ),
                        ) {
                            Text(
                                "Xuất hóa đơn xuất",
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
                        text = "HÓA ĐƠN XUẤT",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(22.dp))
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow("Mã hóa đơn: ", "#${data.id}")
                    InvoiceRow(
                        "Người xuất: ",
                        users[data.user] ?: data.user
                    )
                    InvoiceRow("Ngày xuất: ", formatter.format(Date(data.date)))
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow("Sản phẩm: ", data.productName)

                    InvoiceRow(
                        "Số lượng: ",
                        data.quantity.toString()
                    )
                    InvoiceRow(
                        "Hạn sử dụng: ", data.expiryDate,
                    )
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow2Col(
                        "Khách hàng: ", data.customer,
                        "Số điện thoại: ",
                        data.customerPhone
                    )
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 0.5.dp
                    )

                    InvoiceRow(
                        "Giá xuất: ",
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