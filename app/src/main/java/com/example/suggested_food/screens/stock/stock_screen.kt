package com.example.suggested_food.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.ProductViewModel
import com.example.suggested_food.viewmodels.StockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    stockViewModel: StockViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val totalStock = stockViewModel.totalStock(products)
    val lowStock = stockViewModel.lowStock(products)
    val outStock = stockViewModel.outOfStock(products)

    Scaffold(
        topBar = {

            var menuExpanded by remember { mutableStateOf(false) }

            TopAppBar(
                title = {
                    Text("Quản lý tồn kho", fontWeight = FontWeight.Bold)
                },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                },

                actions = {

                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }

                    StockMenuDropdown(
                        expanded = menuExpanded,
                        onDismiss = { menuExpanded = false },
                        onImport = {
                            menuExpanded = false
                            navController.navigate("ImportStockScreen")
                        },
                        onExport = {
                            menuExpanded = false
                            navController.navigate("export_stock")
                        },
                        onHistory = {
                            menuExpanded = false
                            navController.navigate("stock_history")
                        }
                    )
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
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Tổng số lượng thuốc",
                        value = products.size.toString(),
                        mainColor = Color(0xFF1976D2),
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Tổng số lượng tồn",
                        value = totalStock.toString(),
                        mainColor = Color(0xFF7B1FA2),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Thuốc sắp hết",
                        value = lowStock.size.toString(),
                        mainColor = Color(0xFFF9A825),
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Hết hàng",
                        value = outStock.size.toString(),
                        mainColor = Color(0xFFC62828),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Danh sách tồn kho",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp,
                )

                TextButton(
                    onClick = {
                        navController.navigate("StockAllScreen")
                    }
                ) {
                    Text(
                        text = "Xem tất cả",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            StockListSection(products)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    mainColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),

        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = mainColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(14.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = title,
                    color = mainColor,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = value,
                    color = mainColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}