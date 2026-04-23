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
import androidx.compose.ui.unit.sp
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
    var showSheet by remember { mutableStateOf(false) }

    val filteredProducts = remember(products, selectedRange) {

        when (selectedRange) {

            PriceRange.RANGE_10_20 ->
                products.filter { it.price in 10_000.0..20_000.0 }

            PriceRange.RANGE_20_40 ->
                products.filter { it.price in 20_000.0..40_000.0 }

            PriceRange.RANGE_50_100 ->
                products.filter { it.price in 50_000.0..100_000.0 }

            else -> products
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = Color.White
        ) {
            PriceFilterContent(
                selected = selectedRange,
                onSelect = {
                    selectedRange = it
                    showSheet = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tất cả sản phẩm", fontWeight = FontWeight.Bold)
                },

                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = { showSheet = true }
                    ) {
                        Icon(Icons.Default.MoreVert, null)
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
                CircularProgressIndicator()
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
                            navController.navigate(
                                "ProductDetail/${product.id}"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PriceFilterContent(
    selected: PriceRange,
    onSelect: (PriceRange) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        PriceItem("Tất cả", selected == PriceRange.ALL) {
            onSelect(PriceRange.ALL)
        }

        PriceItem("10,000đ - 20,000đ", selected == PriceRange.RANGE_10_20) {
            onSelect(PriceRange.RANGE_10_20)
        }

        PriceItem("20,000đ - 40,000đ", selected == PriceRange.RANGE_20_40) {
            onSelect(PriceRange.RANGE_20_40)
        }

        PriceItem("50,000đ - 100,000đ", selected == PriceRange.RANGE_50_100) {
            onSelect(PriceRange.RANGE_50_100)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PriceItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = if (selected)
                Color(0xFFEC4899)
            else
                Color.Black,
            fontWeight = if (selected)
                FontWeight.Bold
            else
                FontWeight.Bold
        )
    }
}