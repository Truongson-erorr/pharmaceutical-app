package com.example.suggested_food.screens.invoice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suggested_food.viewmodel.ExportViewModel
import com.example.suggested_food.viewmodel.ImportViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDashboardScreen(
    navController: NavController
) {
    val exportViewModel: ExportViewModel = viewModel()
    val importViewModel: ImportViewModel = viewModel()

    val exports by exportViewModel.exportList.collectAsState()
    val imports by importViewModel.importList.collectAsState()

    val currency = NumberFormat.getInstance(Locale("vi", "VN"))

    LaunchedEffect(Unit) {
        exportViewModel.loadAllExports()
        importViewModel.loadAllImports()
    }

    val importCount = imports.size.toString()
    val exportCount = exports.size.toString()

    val today = System.currentTimeMillis()
    val oneDay = 24 * 60 * 60 * 1000

    val todayCount = (
            exports.count { it.date >= today - oneDay } +
                    imports.count { it.date >= today - oneDay }
            ).toString()

    val totalMoney = currency.format(
        (exports.sumOf { it.totalPrice } +
                imports.sumOf { it.totalPrice })
    ) + " đ"

    val recentActivity = remember(exports, imports) {

        val exportActivity = exports.map {
            Triple("EXPORT", it.productName, it.quantity) to it.date
        }

        val importActivity = imports.map {
            Triple("IMPORT", it.productName, it.quantity) to it.date
        }

        (exportActivity + importActivity)
            .sortedByDescending { it.second }
            .map { it.first }
    }

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(120)
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
        targetValue = if (visible) 0f else 40f,

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
                            "Quản lý hóa đơn",
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
            FloatingActionButton(
                onClick = { navController.navigate("InvoiceScreen") },
                containerColor = Color(0xFF111827)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        "Tạo hóa đơn",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.alpha = alpha
                    this.translationY = translationY
                }
        ) {

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Tổng quan hệ thống",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF111827)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    DashboardCard(
                        "Phiếu nhập",
                        importCount,
                        Icons.Default.Inventory,

                        listOf(
                            Color(0xFFFEE140),
                            Color(0xFFFA709A)
                        ),

                        Modifier.weight(1f)
                    )

                    DashboardCard(
                        "Phiếu xuất",
                        exportCount,
                        Icons.Default.LocalShipping,

                        listOf(
                            Color(0xFFF093FB),
                            Color(0xFFF5576C)
                        ),

                        Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    DashboardCard(
                        "Hôm nay",
                        todayCount,
                        Icons.Default.Today,

                        listOf(
                            Color(0xFF00F2FE),
                            Color(0xFF4FACFE)
                        ),

                        Modifier.weight(1f)
                    )

                    DashboardCard(
                        "Tổng tiền",
                        totalMoney,
                        Icons.Default.AttachMoney,

                        listOf(
                            Color(0xFF4FACFE),
                            Color(0xFF6A11CB)
                        ),

                        Modifier.weight(1f)
                    )
                }

                Text(
                    "Hoạt động gần đây",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    recentActivity.forEachIndexed { index, (type, name, qty) ->

                        var itemVisible by remember(index) {
                            mutableStateOf(false)
                        }

                        LaunchedEffect(index) {
                            delay(index * 55L)
                            itemVisible = true
                        }

                        val itemAlpha by animateFloatAsState(
                            targetValue = if (itemVisible) 1f else 0f,

                            animationSpec = tween(
                                durationMillis = 650,
                                easing = FastOutSlowInEasing
                            ),

                            label = ""
                        )

                        val itemTranslationY by animateFloatAsState(
                            targetValue = if (itemVisible) 0f else 28f,

                            animationSpec = tween(
                                durationMillis = 650,
                                easing = FastOutSlowInEasing
                            ),

                            label = ""
                        )

                        val (icon, color) =
                            if (type == "EXPORT") {
                                Icons.Default.LocalShipping to Color(0xFF4FC3F7)
                            } else {
                                Icons.Default.Inventory to Color(0xFF4FC3F7)
                            }

                        Box(
                            modifier = Modifier.graphicsLayer {
                                this.alpha = itemAlpha
                                this.translationY = itemTranslationY
                            }
                        ) {

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),

                                elevation = CardDefaults.cardElevation(0.dp),

                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),

                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = name,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF111827)
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val (
                                                statusText,
                                                textColor,
                                                bgColor
                                            ) = if (type == "EXPORT") {

                                                Triple(
                                                    "Xuất kho",
                                                    Color(0xFFF59E0B),
                                                    Color(0xFFFFF7D6)
                                                )

                                            } else {

                                                Triple(
                                                    "Nhập kho",
                                                    Color(0xFF7C3AED),
                                                    Color(0xFFEDE9FE)
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        bgColor,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(
                                                        horizontal = 8.dp,
                                                        vertical = 4.dp
                                                    )
                                            ) {

                                                Text(
                                                    text = statusText,
                                                    color = textColor,
                                                    fontWeight = FontWeight.Medium,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                            Spacer(
                                                modifier = Modifier.width(8.dp)
                                            )

                                            Text(
                                                text = "Số lượng: $qty",
                                                color = Color.Black,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    Icon(
                                        Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = Color.LightGray
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