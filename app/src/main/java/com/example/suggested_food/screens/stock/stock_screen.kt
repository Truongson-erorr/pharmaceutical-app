package com.example.suggested_food.screens.stock

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.ProductViewModel
import com.example.suggested_food.viewmodels.StockViewModel
import kotlinx.coroutines.delay

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
    val today = "2026-05-08"

    val expiredStock = products.filter {
        it.expiryDate.isNotBlank() &&
                it.expiryDate < today
    }

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(80)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,

        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

    val translationY by animateFloatAsState(
        targetValue = if (visible) 0f else 35f,

        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

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
                            "Quản lý tồn kho",
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
                .graphicsLayer {
                    this.alpha = alpha
                    this.translationY = translationY
                }
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
                        "Số lượng thuốc",
                        products.size.toString(),
                        listOf(Color(0xFF43E97B), Color(0xFF38F9D7)),
                        Modifier.weight(1f)
                    )

                    StatCard(
                        "Số lượng tồn",
                        totalStock.toString(),
                        listOf(Color(0xFF4FACFE), Color(0xFF6A11CB)),
                        Modifier.weight(1f)
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        "Thuốc sắp hết",
                        lowStock.size.toString(),
                        listOf(Color(0xFFF6D365), Color(0xFFFDA085)),
                        Modifier.weight(1f)
                    )

                    StatCard(
                        "Hết hàng",
                        outStock.size.toString(),
                        listOf(Color(0xFFFF758C), Color(0xFFFF7EB3)),
                        Modifier.weight(1f)
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        "Hết hạn sử dụng",
                        expiredStock.size.toString(),
                        listOf(Color(0xFFEF4444), Color(0xFFFB7185)),
                        Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
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
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val icon = when (title) {
        "Số lượng thuốc" -> Icons.Default.Medication
        "Số lượng tồn" -> Icons.Default.Inventory
        "Thuốc sắp hết" -> Icons.Default.Warning
        "Hết hàng" -> Icons.Default.Error
        else -> Icons.Default.Inventory
    }

    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors))
                .padding(16.dp)
        ) {

            Text(
                text = title,
                modifier = Modifier.align(Alignment.TopStart),
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(44.dp)
                    .background(
                        Color.White.copy(alpha = 0.25f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = value,
                modifier = Modifier.align(Alignment.BottomStart),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

