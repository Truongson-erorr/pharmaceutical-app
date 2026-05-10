package com.example.suggested_food.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.lerp
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
    val patient = patients.find { it.phone == phone }

    val currency = NumberFormat.getInstance(Locale("vi", "VN"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (patient == null) return

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopAppBar(
                title = { Text("Thông tin khách hàng", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            SectionLabel("Thông tin chi tiết")
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

            SectionLabel("Lịch sử mua hàng")
        }
    }
}
