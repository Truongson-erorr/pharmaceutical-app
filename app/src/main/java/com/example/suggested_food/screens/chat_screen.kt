package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.models.ChatMessage
import com.example.suggested_food.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {

        TopAppBar(
            title = {
                Text(
                    "Chat tư vấn thuốc",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Outlined.ArrowBack, null, tint = Color.White)
                }
            },
            actions = {
                Icon(
                    Icons.Outlined.SupportAgent,
                    null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 16.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF8B0000)
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg)
            }
        }

        if (messages.size == 1) {
            QuickSuggestionsHorizontal {
                viewModel.sendMessage(it)
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
                placeholder = { Text("Nhập tin nhắn...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(input)
                        input = ""
                    }
                }
            ) {
                Icon(
                    Icons.Filled.Send,
                    null,
                    tint = Color(0xFF8B0000)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser)
            Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.isUser)
                        Color(0xFF8B0000)
                    else
                        Color(0xFFE5E7EB),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun QuickSuggestionsHorizontal(
    onSelect: (String) -> Unit
) {
    val suggestions = listOf(
        "💊 Công dụng thuốc",
        "📄 Tra cứu đơn thuốc",
        "🤒 Tư vấn triệu chứng",
        "⏰ Cách dùng & liều lượng",
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestions) { text ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .clickable { onSelect(text) }
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = Color(0xFF8B0000),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
