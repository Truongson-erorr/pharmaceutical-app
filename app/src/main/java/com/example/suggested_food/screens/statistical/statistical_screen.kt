package com.example.suggested_food.screens.statistical

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.StatisticalViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticalScreen(
    navController: NavController
) {
    val viewModel: StatisticalViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

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
                            "Báo cáo thống kê",
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
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF5F5F5)
                )
                .padding(paddingValues)
                .padding(16.dp)
                .graphicsLayer {
                    this.alpha = alpha
                    this.translationY = translationY
                },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                SectionTitle("Tổng quan doanh thu")
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        StatCardGradient(
                            modifier = Modifier.weight(1f),
                            title = "Tiền nhập",
                            value = formatMoney(state.totalImportAmount),
                            icon = Icons.Default.ImportExport,
                            colors = listOf(
                                Color(0xFF43E97B),
                                Color(0xFF38F9D7)
                            )
                        )

                        StatCardGradient(
                            modifier = Modifier.weight(1f),
                            title = "Tiền xuất",
                            value = formatMoney(state.totalExportAmount),
                            icon = Icons.Default.Moving,
                            colors = listOf(
                                Color(0xFFFF6B6B),
                                Color(0xFFFFB86B)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        StatCardGradient(
                            modifier = Modifier.weight(1f),
                            title = "Lợi nhuận",
                            value = formatMoney(state.totalProfit),
                            icon = Icons.Default.Star,
                            colors = listOf(
                                Color(0xFF7C3AED),
                                Color(0xFFF472B6)
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                SectionTitle(
                    text = "Top 5 thuốc nhập nhiều nhất",
                    onViewAllClick = {
                        navController.navigate("all_imported")
                    }
                )
            }

            item {
                StatCardChip(items = state.topImported5)
            }

            item {
                SectionTitle(
                    text = "Top 5 thuốc bán nhiều nhất",
                    onViewAllClick = {
                        navController.navigate("all_exported")
                    }
                )
            }

            item {
                StatCardChip(items = state.topExported5)
            }
        }
    }
}
