package com.example.suggested_food.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.suggested_food.models.AppNotification
import com.example.suggested_food.viewmodels.NotificationViewModel
import java.util.Date
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { padding ->

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có thông báo")
            }
        } else {

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(12.dp)
            ) {

                notifications.forEach { item ->

                    NotificationItem(item)
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationItem(item: AppNotification) {

    val color = when (item.type) {
        "IMPORT" -> Color(0xFF1565C0)
        "EXPORT" -> Color(0xFF2E7D32)
        "WARNING" -> Color(0xFFC62828)
        else -> Color(0xFF546E7A)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = item.message,
                color = Color.DarkGray
            )
            Spacer(Modifier.height(6.dp))

            Text(
                text = Date(item.time).toString(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

