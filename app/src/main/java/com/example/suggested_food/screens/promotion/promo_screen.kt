package com.example.suggested_food.screens.promotion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.models.PromoCode
import com.example.suggested_food.viewmodels.PromoCodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoCodeScreen(
    navController: NavController,
    viewModel: PromoCodeViewModel
) {
    val promos by viewModel.promoCodes.collectAsState()
    var selectedDeleteId by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quản lý mã khuyến mãi",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1C1C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIos, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("AddPromoCodeScreen")
                },
                containerColor = Color.Black,
                contentColor = Color.White,
                icon = {
                    Icon(Icons.Default.Add, null)
                },
                text = {
                    Text("Tạo mã khuyến mãi")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(promos) { item ->

                val statusColor = if (item.isActive) Color(0xFF22C55E) else Color.Gray
                val statusBg = if (item.isActive)
                    Color(0xFF22C55E).copy(alpha = 0.15f)
                else
                    Color.Gray.copy(alpha = 0.15f)

                val discountText = when (item.discountType) {
                    "PERCENT" -> "${item.discountValue.toInt()}%"
                    else -> "${item.discountValue.toInt()}đ"
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {

                    Column(modifier = Modifier.padding(14.dp)) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Icon(
                                Icons.Default.LocalOffer,
                                null,
                                tint = Color(0xFFF9A825)
                            )
                            Spacer(Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    text = item.code,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = item.name,
                                    color = Color.Gray
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(statusBg, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (item.isActive) "Active" else "Off",
                                    color = statusColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))

                        Text("Giảm: $discountText", fontWeight = FontWeight.Medium)
                        Text("Đơn tối thiểu: ${item.minOrderValue.toInt()}đ")
                        Text("Đã dùng: ${item.usedCount}")
                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    checked = item.isActive,
                                    onCheckedChange = {
                                        viewModel.toggleActive(item)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color.Black,
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = Color(0xFFBDBDBD)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = Color.White,
                title = { Text("Xóa mã khuyến mãi") },
                text = { Text("Bạn có chắc muốn xóa mã này không?") },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDeleteId?.let {
                            viewModel.deletePromoCode(it)
                        }
                        showDeleteDialog = false
                    }) {
                        Text("Xóa", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}