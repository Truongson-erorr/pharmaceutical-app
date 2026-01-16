package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.models.ChatMessageDoctor
import com.example.suggested_food.viewmodels.ChatDoctorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserChatScreen(
    navController: NavController,
    userId: String,
    viewModel: ChatDoctorViewModel = viewModel()
) {
    val messages = viewModel.messages
    var input by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.listenMessages("chat_$userId")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Chat với bác sĩ",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Outlined.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            actions = {
                Icon(
                    Icons.Outlined.SupervisorAccount,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 16.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1E88E5)
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                MessageBubbleDoctor(msg)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nhập câu hỏi cho bác sĩ...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF1F5F9),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(
                            chatId = "chat_$userId",
                            message = ChatMessageDoctor(
                                senderId = userId,
                                senderRole = "USER",
                                text = input,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        input = ""
                    }
                }
            ) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = null,
                    tint = Color(0xFF1E88E5)
                )
            }
        }
    }
}

@Composable
fun MessageBubbleDoctor(message: ChatMessageDoctor) {
    val isUser = message.senderRole == "USER"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser)
            Alignment.End else Alignment.Start
    ) {

        Box(
            modifier = Modifier
                .background(
                    if (isUser)
                        Color(0xFF1E88E5)
                    else
                        Color(0xFFE5E7EB),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = if (isUser) Color.White else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = formatChatTime(message.timestamp),
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            color = Color.Gray,
            modifier = Modifier.padding(
                start = if (isUser) 0.dp else 8.dp,
                end = if (isUser) 8.dp else 0.dp
            )
        )
    }
}

fun formatChatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
