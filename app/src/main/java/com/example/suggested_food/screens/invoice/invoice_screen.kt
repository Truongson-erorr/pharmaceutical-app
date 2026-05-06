package com.example.suggested_food.screens.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tạo hóa đơn",
                        fontWeight = FontWeight.Bold
                    )
                },
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
                .background(Color(0xFFF6F7F9))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            InvoiceActionCard(
                title = "Nhập thuốc",
                desc = "Tạo phiếu nhập kho",
                borderColor = Color(0xFF10B981),
                backgroundColor = Color(0xFFD1FAE5)
            ) {
                navController.navigate("ImportStockScreen")
            }

            InvoiceActionCard(
                title = "Xuất thuốc",
                desc = "Tạo phiếu xuất kho",
                borderColor = Color(0xFFF97316),
                backgroundColor = Color(0xFFFFEDD5)
            ) {
                navController.navigate("ExportStockScreen")
            }

            InvoiceActionCard(
                title = "Lịch sử nhập / xuất",
                desc = "Xem các giao dịch đã thực hiện",
                borderColor = Color(0xFF3B82F6),
                backgroundColor = Color(0xFFDBEAFE)
            ) {
                navController.navigate("InvoiceHistoryScreen")
            }
        }
    }
}

@Composable
fun InvoiceActionCard(
    title: String,
    desc: String,
    borderColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = borderColor
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    desc,
                    color = borderColor.copy(alpha = 0.8f)
                )
            }

            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = borderColor
            )
        }
    }
}