package com.example.suggested_food.screens.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.models.ProductModel
import com.example.suggested_food.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    val products by inventoryViewModel.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Quản lý kho thuốc", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                actions = {
                    TextButton(
                        onClick = { navController.navigate("InventoryAddScreen") },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color(0xFFE8F5E9)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "Thêm thuốc mới",
                            color = Color(0xFF00C853),
                            fontWeight = FontWeight.SemiBold
                        )
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
                .background(Color(0xFFF5F5F5))
                .padding(12.dp)
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(products) { product ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = product.images.firstOrNull(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(end = 10.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text("Cách dùng: ${product.usage}")
                            }
                        }
                        Spacer(Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .background(
                                    color = Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    navController.navigate("product_detail/${product.id}")
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Chi tiết",
                                color = Color(0xFF0D47A1),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
