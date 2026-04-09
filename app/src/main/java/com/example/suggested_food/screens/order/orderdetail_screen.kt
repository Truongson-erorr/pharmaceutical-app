package com.example.suggested_food.screens.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.OrderHistoryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suggested_food.screens.home.formatVND

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    viewModel: OrderHistoryViewModel = viewModel()
) {
    val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
    val orders by viewModel.orders.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        if (orders.isEmpty()) {
            viewModel.loadOrders(userId)
        }
    }

    val order = orders.find { it.id == orderId }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Chi tiết đơn hàng",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF5848CE)
                )
            )
        },
        containerColor = Color(0xFFE0D9FF)
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5848CE))
            }
            return@Scaffold
        }

        if (order == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Không tìm thấy đơn hàng", color = Color.Gray)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = padding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(90.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Đơn #${order.id.take(6)}", fontWeight = FontWeight.Bold)
                    StatusChip(order.status)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Thời gian giao dự kiến: 1–2 ngày tới", color = Color.Gray)
                    Text("Phí vận chuyển: 30.000₫", color = Color.Gray)
                }
                Divider(thickness = 1.dp, color = Color(0xFFE5E7EB), modifier = Modifier.padding(top = 8.dp))
            }

            items(order.items) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 8.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Medium)
                        Text(
                            "x${item.quantity} • ${formatVND(item.price)}",
                            color = Color.Gray
                        )
                    }

                    Text(
                        formatVND(item.price * item.quantity),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            item {
                Divider(thickness = 1.dp, color = Color(0xFFE5E7EB))
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng thanh toán", fontWeight = FontWeight.SemiBold)
                        Text(
                            formatVND(order.total),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Phương thức thanh toán: ${order.paymentMethod}", color = Color.Gray)
                }
            }
        }
    }
}
