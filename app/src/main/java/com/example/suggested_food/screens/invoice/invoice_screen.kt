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
import androidx.compose.ui.graphics.Brush
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
                    title = {
                        Text(
                            "Tạo hóa đơn",
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
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