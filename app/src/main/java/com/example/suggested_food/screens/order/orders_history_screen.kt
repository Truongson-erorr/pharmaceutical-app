package com.example.suggested_food.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.models.OrderModel
import com.example.suggested_food.viewmodels.OrderHistoryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.suggested_food.screens.home.formatVND
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    viewModel: OrderHistoryViewModel = viewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val orders by viewModel.orders.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        "Chờ xác nhận",
        "Đã xác nhận",
        "Đang giao",
        "Đã giao"
    )

    LaunchedEffect(Unit) {
        viewModel.loadOrders(userId)
    }

    Scaffold(
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = padding.calculateBottomPadding())
        ) {

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = Color.White,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF5848CE)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5848CE))
                }
                return@Column
            }

            val filteredOrders = when (selectedTab) {
                0 -> orders.filter { it.status == "PENDING" }
                1 -> orders.filter { it.status == "PAID" || it.status == "CONFIRMED" }
                2 -> orders.filter { it.status == "SHIPPING" }
                3 -> orders.filter { it.status == "DELIVERED" }
                else -> emptyList()
            }

            if (filteredOrders.isEmpty()) {
                OrderEmptyState(
                    message = "Chưa có đơn ${tabs[selectedTab].lowercase()}"
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredOrders) { order ->
                        OrderHistoryCard(order) {
                            navController.navigate("order_detail/${order.id}")
                        }
                        Divider(
                            thickness = 4.dp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: OrderModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.Transparent)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Đơn #${order.id.take(6)}",
                fontWeight = FontWeight.Bold
            )

            StatusChip(order.status)
        }
        Spacer(modifier = Modifier.height(12.dp))

        order.items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = item.image,
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 8.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.name,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "x${item.quantity} • ${formatVND(item.price)}",
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

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
    }
}

@Composable
fun StatusChip(status: String) {
    val (mainColor, text) = when (status) {
        "PENDING" -> Color(0xFFF59E0B) to "Chờ xác nhận"
        "CONFIRMED" -> Color(0xFF2563EB) to "Đã xác nhận"
        "SHIPPING" -> Color(0xFFEF4444) to "Đang giao"
        "DELIVERED" -> Color(0xFF16A34A) to "Đã giao"
        "PAID" -> Color(0xFF16A34A) to "Đã xác nhận"
        else -> Color.Gray to status
    }

    Box(
        modifier = Modifier
            .background(
                color = mainColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = mainColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

