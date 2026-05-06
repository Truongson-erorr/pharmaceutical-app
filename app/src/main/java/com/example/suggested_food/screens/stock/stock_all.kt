package com.example.suggested_food.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FilterList
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
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockAllScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()

    var filter by remember { mutableStateOf("ALL") }
    var tempFilter by remember { mutableStateOf("ALL") }
    var showDialog by remember { mutableStateOf(false) }

    val mainColor = Color.DarkGray
    val lightColor = Color(0xFFF5F5F5)

    val filteredProducts = when (filter) {
        "AVAILABLE" -> products.filter { it.stock > 10 }
        "LOW" -> products.filter { it.stock in 1..10 }
        "OUT" -> products.filter { it.stock == 0 }
        else -> products
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White,
            title = {
                Text("Lọc tồn kho", fontWeight = FontWeight.Bold)
            },
            text = {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    FilterItem(
                        text = "Tất cả",
                        selected = tempFilter == "ALL",
                        mainColor = mainColor,
                        lightColor = lightColor,
                        modifier = Modifier.fillMaxWidth()
                    ) { tempFilter = "ALL" }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        FilterItem(
                            text = "Còn hàng",
                            selected = tempFilter == "AVAILABLE",
                            mainColor = mainColor,
                            lightColor = lightColor,
                            modifier = Modifier.weight(1f)
                        ) { tempFilter = "AVAILABLE" }

                        FilterItem(
                            text = "Sắp hết",
                            selected = tempFilter == "LOW",
                            mainColor = mainColor,
                            lightColor = lightColor,
                            modifier = Modifier.weight(1f)
                        ) { tempFilter = "LOW" }
                    }

                    FilterItem(
                        text = "Hết hàng",
                        selected = tempFilter == "OUT",
                        mainColor = mainColor,
                        lightColor = lightColor,
                        modifier = Modifier.fillMaxWidth()
                    ) { tempFilter = "OUT" }
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color.Black, RoundedCornerShape(12.dp))
                        .clickable {
                            filter = tempFilter
                            showDialog = false
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Xác nhận", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tất cả tồn kho", fontWeight = FontWeight.Bold)
                },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },

                actions = {
                    IconButton(onClick = {
                        tempFilter = filter
                        showDialog = true
                    }) {
                        Icon(Icons.Default.FilterList, null)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredProducts) { item ->

                val statusColor = when {
                    item.stock == 0 -> Color(0xFFFF5A5F)
                    item.stock <= 10 -> Color(0xFFFFB020)
                    else -> Color(0xFF22C55E)
                }

                val statusText = when {
                    item.stock == 0 -> "Hết hàng"
                    item.stock <= 10 -> "Sắp hết"
                    else -> "Còn hàng"
                }

                val statusBg = statusColor.copy(alpha = 0.12f)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(statusBg, RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(statusText, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 70.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = item.images.firstOrNull(),
                                contentDescription = null,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold)
                                Text("Tồn: ${item.stock}", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterItem(
    text: String,
    selected: Boolean,
    mainColor: Color,
    lightColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = lightColor,
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (selected)
                    Modifier.border(2.dp, mainColor, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable { onClick() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = mainColor,
            fontWeight = FontWeight.Bold
        )
    }
}