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
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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

    // FILTER TYPES
    val tabs = listOf(
        "Tất cả",
        "IMPORT",
        "EXPORT",
        "PRODUCT_ADD",
        "PRODUCT_UPDATE",
        "PRODUCT_DELETE"
    )

    var selectedTab by remember {
        mutableStateOf(0)
    }

    val filteredLogs = remember(
        logs,
        selectedTab
    ) {

        when (selectedTab) {

            0 -> logs

            else -> logs.filter {
                it.type == tabs[selectedTab]
            }
        }
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
                            text = "Nhật ký hoạt động",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
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
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .graphicsLayer {
                    alpha = screenAlpha
                    translationY = screenTranslationY
                }
                .background(Color(0xFFF5F5F5))
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 12.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->

                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(
                                tabPositions[selectedTab]
                            )
                            .padding(horizontal = 16.dp)
                            .height(3.dp)
                            .background(
                                Color(0xFF2563EB),
                                RoundedCornerShape(50)
                            )
                    )
                }
            ) {

                tabs.forEachIndexed { index, title ->
                    val selected = selectedTab == index
                    Tab(
                        selected = selected,

                        onClick = {
                            selectedTab = index
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color(0xFF9CA3AF),
                        modifier = Modifier.height(46.dp),

                        text = {
                            Text(
                                text = when (title) {
                                    "IMPORT" -> "Nhập kho"
                                    "EXPORT" -> "Xuất kho"
                                    "PRODUCT_ADD" ->
                                        "Thuốc đã thêm"
                                    "PRODUCT_UPDATE" ->
                                        "Thuốc đã cập nhật"
                                    "PRODUCT_DELETE" ->
                                        "Thuốc đã bị xóa"
                                    else -> title
                                },

                                fontWeight = if (selected)
                                    FontWeight.Bold
                                else
                                    FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (filteredLogs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    Color(0xFFE0F2FE),
                                    shape = RoundedCornerShape(100)
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = Color(0xFF0EA5E9),
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Không có dữ liệu",
                            color = Color.Gray,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Hiện chưa có hoạt động nào!",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),

                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(filteredLogs) { log ->

                        ActivityLogItem(
                            log = log,
                            userName = users[log.userId]
                                ?: "Unknown"
                        )
                    }
                }
            }
        }
    }
}
