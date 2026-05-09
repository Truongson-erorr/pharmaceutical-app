package com.example.suggested_food.screens.reminder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodel.ReminderViewModel
import java.util.Date
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel
) {
    val reminders by viewModel.reminders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lịch nhắc nhở",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBackIos,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("AddReminderScreen") },
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Tạo lịch nhắc")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFF5F5F5))
                .fillMaxSize()
        ) {
            items(reminders) { item ->

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {

                        Text(
                            item.title,
                            fontWeight = FontWeight.Bold
                        )

                        item.description?.let {
                            Text("📝 $it", color = Color.Gray)
                        }

                        Spacer(Modifier.height(4.dp))

                        Text("💊 Thuốc: ${item.medicineName ?: "Không có"}")
                        Text("📦 Loại: ${item.actionType}")
                        Text("⏰ ${Date(item.triggerTime)}")
                        Spacer(Modifier.height(8.dp))

                        Row {

                            if (!item.isDone) {
                                TextButton(onClick = { viewModel.markDone(item.id) }) {
                                    Text("Đã xong")
                                }
                            } else {
                                Text("✔ Done", color = Color.Green)
                            }
                            Spacer(Modifier.weight(1f))

                            TextButton(onClick = { viewModel.deleteReminder(item.id) }) {
                                Text("Xóa", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}