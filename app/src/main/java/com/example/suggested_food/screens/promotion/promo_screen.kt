package com.example.suggested_food.screens.promotion

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.models.PromoCode
import com.example.suggested_food.viewmodels.PromoCodeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoCodeScreen(
    navController: NavController,
    viewModel: PromoCodeViewModel
) {

    val promos by viewModel.promoCodes.collectAsState()

    var selectedDeleteId by remember {
        mutableStateOf<String?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    val screenAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    val screenTranslationY by animateFloatAsState(
        targetValue = if (visible) 0f else 35f,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5),

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
                            "Mã khuyến mãi",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },

                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
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
                onClick = {
                    navController.navigate("AddPromoCodeScreen")
                },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Tạo mã khuyến mãi",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .graphicsLayer {
                    alpha = screenAlpha
                    translationY = screenTranslationY
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(promos, key = { it.id }) { item ->

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->

                        if (value == SwipeToDismissBoxValue.EndToStart) {

                            selectedDeleteId = item.id
                            showDeleteDialog = true
                        }

                        false
                    }
                )
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        DeleteBackground()
                    },
                    content = {

                        PromoCard(
                            item = item,
                            onToggle = {
                                viewModel.toggleActive(item)
                            }
                        )
                    }
                )
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        "Xóa mã khuyến mãi",
                        fontWeight = FontWeight.Bold
                    )
                },

                text = {
                    Text("Bạn có chắc muốn xóa mã này không?")
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            selectedDeleteId?.let {
                                viewModel.deletePromoCode(it)
                            }

                            showDeleteDialog = false
                        }
                    ) {

                        Text(
                            "Xóa",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text(
                            "Hủy",
                            color = Color.Gray
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PromoCard(
    item: PromoCode,
    onToggle: () -> Unit
) {
    val statusBg =
        if (item.isActive)
            Color(0xFFEFFAF3)
        else
            Color(0xFFF1F5F9)

    val statusColor =
        if (item.isActive)
            Color(0xFF16A34A)
        else
            Color(0xFF64748B)

    val discountText = when (item.discountType) {
        "PERCENT" -> "${item.discountValue.toInt()}%"
        else -> "${item.discountValue.toInt()}đ"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF7ED))
                        .padding(10.dp)
                ) {

                    Icon(
                        Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = item.code,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.name,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(statusBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {

                    Text(
                        text = if (item.isActive)
                            "Đang bật"
                        else
                            "Đã tắt",

                        color = statusColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Giảm $discountText",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Đơn tối thiểu ${item.minOrderValue.toInt()}đ",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Text(
                text = "Đã dùng ${item.usedCount} lượt",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFEF4444))
            .padding(horizontal = 20.dp),

        contentAlignment = Alignment.CenterEnd
    ) {

        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}