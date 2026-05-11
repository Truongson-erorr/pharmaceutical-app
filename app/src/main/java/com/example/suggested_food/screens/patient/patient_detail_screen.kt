package com.example.suggested_food.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.PatientViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    navController: NavController,
    phone: String
) {
    val viewModel: PatientViewModel = viewModel()
    val patients by viewModel.patients.collectAsState()
    val receipts by viewModel.patientReceipts.collectAsState()

    val patient = patients.find { it.phone == phone } ?: return

    val currency = NumberFormat.getInstance(Locale("vi", "VN"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(phone) {
        viewModel.loadPatientReceipts(phone)
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopAppBar(
                title = { Text("Thông tin khách hàng", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE3F2FD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                null,
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))

                        Text(
                            patient.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Row {
                            Text("Số điện thoại: ", color = Color.Gray)
                            Text(patient.phone, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            item { SectionLabel("Thông tin chi tiết") }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    InfoItem(
                        title = "Số đơn hàng",
                        value = "${patient.totalOrders}",
                        valueColor = Color(0xFF1D4ED8),
                        modifier = Modifier.weight(1f)
                    )

                    InfoItem(
                        title = "Tổng chi tiêu",
                        value = "${currency.format(patient.totalSpent)} đ",
                        valueColor = Color(0xFF22C55E),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    InfoItem(
                        title = "Lần mua gần nhất",
                        value = if (patient.lastVisit != 0L)
                            dateFormat.format(Date(patient.lastVisit))
                        else "--",
                        valueColor = Color(0xFF7C3AED),
                        modifier = Modifier.weight(1f)
                    )

                    InfoItem(
                        title = "Ngày tạo hồ sơ",
                        value = if (patient.createdAt != 0L)
                            dateFormat.format(Date(patient.createdAt))
                        else "--",
                        valueColor = Color(0xFFEA580C),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { SectionLabel("Lịch sử mua hàng") }
            items(receipts) { receipt ->

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                receipt.productName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFFE3F2FD))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    dateFormat.format(Date(receipt.date)),
                                    color = Color(0xFF1565C0),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        Divider(color = Color(0xFFF1F5F9))
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Số lượng", fontSize = 12.sp, color = Color.Gray)
                                Text("${receipt.quantity}", fontWeight = FontWeight.SemiBold)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Thành tiền", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    "${currency.format(receipt.totalPrice)} đ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF16A34A),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}