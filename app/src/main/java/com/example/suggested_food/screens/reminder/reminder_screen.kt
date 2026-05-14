package com.example.suggested_food.screens.reminder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.ReminderViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel
) {
    val reminders by viewModel.reminders.collectAsState()
    val primary = Color(0xFFF9A825)
    val red = Color(0xFFDC2626)
    val green = Color(0xFF22C55E)
    val cyan = Color(0xFF06B6D4)
    var selectedReminderId by remember { mutableStateOf<String?>(null) }
    var showDoneDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expandedMenuId by remember { mutableStateOf<String?>(null) }

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
                onClick = { navController.navigate("AddReminderScreen") },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("Tạo lịch nhắc")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(reminders) { item ->
                val now = System.currentTimeMillis()
                val diffDays =
                    ((item.triggerTime - now) / (1000 * 60 * 60 * 24)).toInt()

                val timeText = when {
                    diffDays > 0 -> "Nhắc nhở sau $diffDays ngày"
                    diffDays < 0 -> "Đã trễ ${abs(diffDays)} ngày"
                    else -> "Nhắc hôm nay"
                }

                val statusColor = if (item.isDone) green else red
                val statusBg = if (item.isDone)
                    green.copy(alpha = 0.15f)
                else
                    red.copy(alpha = 0.15f)

                val statusText = if (item.isDone)
                    "Đã hoàn thành"
                else
                    "Chưa hoàn thành"

                Box {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(Modifier.weight(1f)) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                                        Text(
                                            text = "Tiêu đề: ${item.title}",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Nội dung: ${item.description ?: "Không có"}",
                                            color = Color.Gray,
                                        )

                                        Text(
                                            text = "Loại nhắc nhở: ${
                                                when (item.actionType) {
                                                    "STOCK_IN" -> "Nhập thuốc"
                                                    "STOCK_OUT" -> "Xuất thuốc"
                                                    else -> "Khác"
                                                }
                                            }",
                                            color = Color.Gray,
                                        )

                                        Text(
                                            text = "Thuốc: ${item.medicineName ?: "Không có"}",
                                            color = Color.Gray,
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = cyan.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(30.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = timeText,
                                            color = cyan,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = MaterialTheme.typography.labelMedium.fontSize
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .offset(y = 4.dp)
                                        .clip(CircleShape)
                                        .background(primary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.NotificationsActive,
                                        null,
                                        tint = primary
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .clickable {
                                        if (!item.isDone) {
                                            selectedReminderId = item.id
                                            showDoneDialog = true
                                        }
                                    },
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .background(statusBg, RoundedCornerShape(30.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = statusText,
                                        fontSize = 13.sp,
                                        color = statusColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                if (!item.isDone) {
                                    Spacer(Modifier.width(4.dp))
                                    Icon(Icons.Default.ChevronRight, null, tint = red)
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        IconButton(onClick = {
                            expandedMenuId = item.id
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            expanded = expandedMenuId == item.id,
                            onDismissRequest = { expandedMenuId = null },
                            modifier = Modifier
                                .background(Color.White)
                                .clip(RoundedCornerShape(50.dp))
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Xóa", color = Color.Black)
                                },
                                onClick = {
                                    expandedMenuId = null
                                    selectedReminderId = item.id
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDoneDialog) {
            AlertDialog(
                onDismissRequest = { showDoneDialog = false },
                containerColor = Color.White,
                title = { Text("Xác nhận") },
                text = { Text("Bạn có chắc muốn đánh dấu hoàn thành?") },
                confirmButton = {
                    TextButton(onClick = {
                        selectedReminderId?.let { viewModel.markDone(it) }
                        showDoneDialog = false
                    }) {
                        Text("Đồng ý")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDoneDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = Color.White,
                title = { Text("Xóa nhắc nhở") },
                text = { Text("Bạn có chắc muốn xóa nhắc nhở này không?") },
                confirmButton = {
                    TextButton(onClick = {
                        selectedReminderId?.let { viewModel.deleteReminder(it) }
                        showDeleteDialog = false
                    }) {
                        Text("Xóa", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}