package com.example.suggested_food.screens.chat_ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.models.CartItemModel
import com.example.suggested_food.models.ChatMessage
import com.example.suggested_food.models.ProductModel
import com.example.suggested_food.viewmodels.CartViewModel
import com.example.suggested_food.viewmodels.ChatViewModel
import com.example.suggested_food.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val messages by chatViewModel.messages.collectAsState()
    val products by productViewModel.products.collectAsState()

    val productNames = products.map { it.name }
    var input by remember { mutableStateOf("") }

    var suggestedProducts by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }
    val isLoading by chatViewModel.isLoading.collectAsState()
    val cartViewModel: CartViewModel = viewModel()

    var showHistorySheet by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        chatViewModel.addToCartEvent.collect { product ->
            cartViewModel.addToCart(
                CartItemModel(
                    productId = product.id,
                    name = product.name,
                    image = product.images.firstOrNull() ?: "",
                    price = product.price,
                    quantity = 1
                )
            )
        }
    }

    LaunchedEffect(messages) {
        val lastBotMessage = messages.lastOrNull { !it.isUser } ?: return@LaunchedEffect

        val drugNames = extractDrugNames(lastBotMessage.text)

        suggestedProducts = products.filter { product ->
            drugNames.any { it.equals(product.name, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            )
    ) {

        TopAppBar(
            title = {
                Text(
                    "AI gợi ý thuốc",
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
                IconButton(
                    onClick = { showHistorySheet = true }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF5848CE)
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(messages) { index, msg ->
                ChatBubble(msg)

                val isLastBotMessage =
                    !msg.isUser && index == messages.lastIndex

                if (isLastBotMessage && suggestedProducts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(suggestedProducts) { product ->
                            ProductSuggestionCard(
                                product = product,
                                onClick = {
                                    navController.navigate("ProductDetail/${product.id}")
                                }
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    ChatBubble(
                        ChatMessage(
                            text = "…",
                            isUser = false
                        )
                    )
                }
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
                placeholder = { Text("Nhập triệu chứng hoặc câu hỏi...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(
                enabled = !isLoading,
                onClick = {
                    if (input.isNotBlank()) {
                        chatViewModel.sendMessage(
                            userMsg = input,
                            productNames = productNames,
                            allProducts = products
                        )
                        input = ""
                    }
                }
            ) {
                Icon(
                    Icons.Filled.Send,
                    null,
                    tint = Color(0xFF5848CE)
                )
            }
        }

        if (showHistorySheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showHistorySheet = false
                },
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "History",
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            "Lịch sử gợi ý thuốc",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isTyping = message.text == "…"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser)
            Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.isUser)
                        Color(0xFF5848CE)
                    else
                        Color(0xFFF1F5F9),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            if (isTyping) {
                TypingDots()
            } else {
                Text(
                    text = message.text,
                    color = if (message.isUser) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun TypingDots() {
    var dots by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            dots = when (dots.length) {
                0 -> "."
                1 -> ".."
                2 -> "..."
                else -> ""
            }
            kotlinx.coroutines.delay(400)
        }
    }

    Text(
        text = dots,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )
}

fun extractDrugNames(text: String): List<String> {
    return text.lines()
        .mapNotNull { line ->
            Regex("""^\s*\d+\.\s*(.+)$""")
                .find(line)
                ?.groupValues
                ?.get(1)
                ?.trim()
        }
        .distinct()
}

@Composable
fun ProductSuggestionCard(
    product: ProductModel,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${product.price} ₫",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
