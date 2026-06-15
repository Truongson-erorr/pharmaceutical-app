package com.example.suggested_food.screens.stock

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
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
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.ProductViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockAllScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    var filter by remember { mutableStateOf("ALL") }
    var showDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val filteredProducts = when (filter) {

        "AVAILABLE" ->
            products.filter { it.stock > 10 }

        "LOW" ->
            products.filter { it.stock in 1..10 }

        "OUT" ->
            products.filter { it.stock == 0 }

        "EXPIRING" ->
            products.filter {
                isExpiringSoon(it.expiryDate)
            }

        "EXPIRED" ->
            products.filter {
                isExpired(it.expiryDate)
            }

        else ->
            products
    }

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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {
                        Text(
                            "Tất cả sản phẩm tồn kho",
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
                    actions = {

                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = "Filter",
                                tint = Color.White
                            )
                        }

                    }
                )
            }
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

            itemsIndexed(filteredProducts) { index, item ->
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

                val expiryColor = when {

                    isExpired(item.expiryDate) ->
                        Color(0xFFFF5A5F)

                    isExpiringSoon(item.expiryDate) ->
                        Color(0xFFFFB020)

                    else ->
                        Color(0xFF22C55E)
                }
                val expiryBg = expiryColor.copy(alpha = 0.12f)

                Box(
                    modifier = Modifier.graphicsLayer {
                        this.alpha = alpha
                        this.translationY = translationY
                    }
                ) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
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
                                    .background(
                                        statusBg,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    )
                            ) {
                                Text(
                                    statusText,
                                    color = statusColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
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

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {

                                    Text(
                                        item.name,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "Tồn: ${item.stock}",
                                        color = Color.Gray
                                    )

                                    if (item.expiryDate.isNotBlank()) {

                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    expiryBg,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(
                                                    horizontal = 8.dp,
                                                    vertical = 3.dp
                                                )
                                        ) {

                                            Text(
                                                "Hạn sử dụng: ${item.expiryDate}",
                                                color = expiryColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {

        ModalBottomSheet(
            onDismissRequest = {
                showDialog = false
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {

                Text(
                    text = "Lọc theo trạng thái",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(20.dp))

                FilterMenuItem(
                    title = "Tất cả thuốc trong kho",
                    selected = filter == "ALL"
                ) {
                    filter = "ALL"
                    showDialog = false
                }

                FilterMenuItem(
                    title = "Thuốc còn hàng",
                    selected = filter == "AVAILABLE"
                ) {
                    filter = "AVAILABLE"
                    showDialog = false
                }

                FilterMenuItem(
                    title = "Thuốc sắp hết hàng",
                    selected = filter == "LOW"
                ) {
                    filter = "LOW"
                    showDialog = false
                }

                FilterMenuItem(
                    title = "Thuốc đã hết hàng",
                    selected = filter == "OUT"
                ) {
                    filter = "OUT"
                    showDialog = false
                }

                FilterMenuItem(
                    title = "Thuốc sắp hết hạn sử dụng",
                    selected = filter == "EXPIRING"
                ) {
                    filter = "EXPIRING"
                    showDialog = false
                }

                FilterMenuItem(
                    title = "Thuốc đã hết hạn sử dụng",
                    selected = filter == "EXPIRED"
                ) {
                    filter = "EXPIRED"
                    showDialog = false
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
