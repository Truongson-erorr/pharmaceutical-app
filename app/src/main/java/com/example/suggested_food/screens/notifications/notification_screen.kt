package com.example.suggested_food.screens.notifications

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suggested_food.viewmodels.NotificationViewModel
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    var expandedIds by remember { mutableStateOf(setOf<String>()) }
    var isLoading by remember { mutableStateOf(true) }

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            delay(80)
            visible = true
        }
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = ""
    )

    val animatedTranslationY by animateFloatAsState(
        targetValue = if (visible) 0f else 35f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = ""
    )

    LaunchedEffect(Unit) {

        viewModel.loadNotifications()
        delay(1500)

        isLoading = false
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
                    title = {
                        Text(
                            "Thông báo",
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

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            if (isLoading) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(10) {
                        ShimmerNotificationCard()
                    }
                }
            }

            else {
                if (notifications.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có thông báo")
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .graphicsLayer {
                                alpha = animatedAlpha
                                translationY = animatedTranslationY
                            },
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        itemsIndexed(
                            notifications,
                            key = { _, item -> item.id }
                        ) { index, item ->

                            NotificationItem(
                                item = item,
                                isExpanded = expandedIds.contains(item.id),
                                onToggle = {
                                    expandedIds =
                                        if (expandedIds.contains(item.id)) {
                                            expandedIds - item.id
                                        } else {
                                            expandedIds + item.id
                                        }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerNotificationCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 85.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .shimmer(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.LightGray, CircleShape)
                )
                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {

                    Box(
                        Modifier
                            .fillMaxWidth(0.7f)
                            .height(18.dp)
                            .background(
                                Color.LightGray,
                                RoundedCornerShape(8.dp)
                            )
                    )
                    Spacer(Modifier.height(8.dp))

                    Box(
                        Modifier
                            .fillMaxWidth(0.4f)
                            .height(17.dp)
                            .background(
                                Color.LightGray,
                                RoundedCornerShape(6.dp)
                            )
                    )
                }
            }
        }
    }
}

fun formatTime(time: Long): String {

    val diff = System.currentTimeMillis() - time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 10 -> "Vừa xong"
        seconds < 60 -> "$seconds giây trước"
        minutes < 60 -> "$minutes phút trước"
        hours < 24 -> "$hours giờ trước"
        days < 7 -> "$days ngày trước"
        else -> formatDate(time)
    }
}