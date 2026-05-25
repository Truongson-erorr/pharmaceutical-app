package com.example.suggested_food.screens.reminder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.ReminderViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.abs
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel
) {
    val reminders by viewModel.reminders.collectAsState()

    var selectedId by remember { mutableStateOf<String?>(null) }
    var showDoneDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuId by remember { mutableStateOf<String?>(null) }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val filteredReminders = reminders.filter {

        Instant.ofEpochMilli(it.triggerTime)
            .atZone(ZoneId.systemDefault())
            .toLocalDate() == selectedDate
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
        containerColor = Color.White,
        topBar = @androidx.compose.runtime.Composable {
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
                            "Lịch nhắc nhở",
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
                onClick = {
                    navController.navigate("AddReminderScreen")
                },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Tạo lịch nhắc",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .graphicsLayer {
                    alpha = screenAlpha
                    translationY = screenTranslationY
                }
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            WeekCalendar(
                selectedDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                }
            )
            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredReminders.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                Icons.Default.EventBusy,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Không có lịch nhắc",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "Hãy tạo lịch mới cho ngày này",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                } else {

                    items(filteredReminders, key = { it.id }) { r ->

                        val state = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    selectedId = r.id
                                    showDeleteDialog = true
                                }
                                false
                            }
                        )

                        SwipeToDismissBox(
                            state = state,
                            backgroundContent = {
                                DeleteBackground()
                            },
                            content = {
                                ReminderCard(
                                    reminder = r,
                                    isMenuOpen = menuId == r.id,
                                    onMenuClick = { menuId = r.id },
                                    onDoneClick = {
                                        selectedId = r.id
                                        showDoneDialog = true
                                    },
                                    onDeleteClick = {
                                        selectedId = r.id
                                        showDeleteDialog = true
                                    },
                                    onDismissMenu = { menuId = null }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDoneDialog) {
        SimpleDialog(
            title = "Hoàn thành?",
            text = "Đánh dấu đã hoàn thành nhắc nhở này?",
            confirmText = "Xác nhận",
            onConfirm = {
                selectedId?.let { viewModel.markDone(it) }
                showDoneDialog = false
            },
            onDismiss = { showDoneDialog = false }
        )
    }

    if (showDeleteDialog) {
        SimpleDialog(
            title = "Xóa nhắc nhở?",
            text = "Hành động không thể khôi phục.",
            confirmText = "Xóa",
            confirmColor = Color.Red,
            onConfirm = {
                selectedId?.let { viewModel.deleteReminder(it) }
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}