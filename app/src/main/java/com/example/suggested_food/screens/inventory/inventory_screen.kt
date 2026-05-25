package com.example.suggested_food.screens.inventory

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.InventoryViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    val products by inventoryViewModel.products.collectAsState()
    val currency = NumberFormat.getInstance(Locale("vi", "VN"))

    Scaffold(
        containerColor = Color.White,

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
                            "Quản lý kho thuốc",
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
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("InventoryAddScreen") },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Thêm thuốc", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            itemsIndexed(products) { index, product ->

                var visible by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {
                    delay(index * 55L)
                    visible = true
                }

                val alpha by animateFloatAsState(
                    targetValue = if (visible) 1f else 0f,

                    animationSpec = tween(
                        durationMillis = 650,
                        easing = FastOutSlowInEasing
                    ),

                    label = ""
                )

                val translationY by animateFloatAsState(
                    targetValue = if (visible) 0f else 28f,

                    animationSpec = tween(
                        durationMillis = 650,
                        easing = FastOutSlowInEasing
                    ),

                    label = ""
                )

                val expiryColor = getExpiryColor(product.expiryDate)

                Box(
                    modifier = Modifier.graphicsLayer {
                        this.alpha = alpha
                        this.translationY = translationY
                    }
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("product_detail/${product.id}")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {

                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = product.images.firstOrNull(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(end = 12.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {

                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "Giá: ${currency.format(product.price)} đ",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .background(
                                            color = expiryColor.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                ) {
                                    Text(
                                        text = "HSD: ${product.expiryDate}",
                                        color = expiryColor,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getExpiryColor(dateStr: String?): Color {
    if (dateStr == null) return Color.Gray

    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val expiry = sdf.parse(dateStr) ?: return Color.Gray

        val now = Date()
        val diff = expiry.time - now.time

        when {
            diff < 0 ->
                Color(0xFFFF5A5F)

            diff <= 7L * 24 * 60 * 60 * 1000 ->
                Color(0xFFFFB020)

            else ->
                Color(0xFF22C55E)
        }

    } catch (e: Exception) {
        Color.Gray
    }
}