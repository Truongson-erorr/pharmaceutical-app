package com.example.suggested_food.screens.activitylog

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
import com.example.suggested_food.viewmodels.ActivityLogViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLogScreen(
    navController: NavController,
    viewModel: ActivityLogViewModel = viewModel()
) {
    val logs by viewModel.logs.collectAsState()
    val users by viewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
        viewModel.loadLogs()
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
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2563EB), Color(0xFF38BDF8))
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Nhật ký hoạt động",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
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
                .fillMaxSize()
                .graphicsLayer {
                    alpha = screenAlpha
                    translationY = screenTranslationY
                }
                .background(Color(0xFFF5F5F5))
                .padding(padding)
        ) {

            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chưa có hoạt động nào",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(logs) { log ->
                        ActivityLogItem(
                            log = log,
                            userName = users[log.userId] ?: "Unknown"
                        )
                    }
                }
            }
        }
    }
}
