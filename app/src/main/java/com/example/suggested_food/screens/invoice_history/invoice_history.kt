package com.example.suggested_food.screens.invoice_history

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodel.StockHistoryViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceHistoryScreen(
    navController: NavController
) {
    val viewModel: StockHistoryViewModel = viewModel()
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        "Lịch sử nhập",
        "Lịch sử xuất"
    )

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(120)
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
        targetValue = if (visible) 0f else 40f,

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
                            "Lịch sử nhập / xuất",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
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
                .fillMaxSize()
                .graphicsLayer {
                    alpha = screenAlpha
                    translationY = screenTranslationY
                }
        ) {

            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),

                    shape = RoundedCornerShape(30.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    TabRow(
                        selectedTabIndex = tabIndex,
                        containerColor = Color.Transparent,
                        indicator = {},
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, title ->

                            val selected = tabIndex == index

                            Tab(
                                selected = selected,

                                onClick = {
                                    tabIndex = index
                                },

                                modifier = Modifier
                                    .padding(6.dp)
                                    .height(42.dp)
                                    .background(
                                        if (selected)
                                            Color.Black
                                        else
                                            Color.Transparent,

                                        RoundedCornerShape(50)
                                    ),

                                text = {
                                    Text(
                                        title,

                                        color = if (selected)
                                            Color.White
                                        else
                                            Color.Black,

                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                Box(
                    Modifier.fillMaxSize()
                ) {

                    when (tabIndex) {

                        0 -> ImportHistoryScreen(
                            navController,
                            viewModel
                        )

                        1 -> ExportHistoryScreen(
                            navController,
                            viewModel
                        )
                    }
                }
            }
        }
    }
}