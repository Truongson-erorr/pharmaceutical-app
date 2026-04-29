package com.example.suggested_food.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.ProductViewModel

enum class PriceRange {
    ALL,
    RANGE_10_20,
    RANGE_20_40,
    RANGE_50_100
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductScreen(
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    val products by viewModel.products.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var selectedRange by remember { mutableStateOf(PriceRange.ALL) }
    var showMenu by remember { mutableStateOf(false) }

    val filteredProducts = remember(products, selectedRange) {
        when (selectedRange) {
            PriceRange.RANGE_10_20 -> products.filter { it.price in 10_000.0..20_000.0 }
            PriceRange.RANGE_20_40 -> products.filter { it.price in 20_000.0..40_000.0 }
            PriceRange.RANGE_50_100 -> products.filter { it.price in 50_000.0..100_000.0 }
            else -> products
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tất cả sản phẩm", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                ),
                actions = {

                    Box {

                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, null)
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .background(Color.White)
                                .width(180.dp)
                        ) {

                            PriceDropdownItem(
                                text = "Tất cả",
                                selected = selectedRange == PriceRange.ALL
                            ) {
                                selectedRange = PriceRange.ALL
                                showMenu = false
                            }

                            PriceDropdownItem(
                                text = "10k - 20k",
                                selected = selectedRange == PriceRange.RANGE_10_20
                            ) {
                                selectedRange = PriceRange.RANGE_10_20
                                showMenu = false
                            }

                            PriceDropdownItem(
                                text = "20k - 40k",
                                selected = selectedRange == PriceRange.RANGE_20_40
                            ) {
                                selectedRange = PriceRange.RANGE_20_40
                                showMenu = false
                            }

                            PriceDropdownItem(
                                text = "50k - 100k",
                                selected = selectedRange == PriceRange.RANGE_50_100
                            ) {
                                selectedRange = PriceRange.RANGE_50_100
                                showMenu = false
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFEC4899))
            }
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .background(Color(0xFFF5F5F5))
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductGridItem(
                        product = product,
                        onClick = {
                            navController.navigate("ProductDetail/${product.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PriceDropdownItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color.Black else Color.Black
            )
        },
        onClick = onClick
    )
}