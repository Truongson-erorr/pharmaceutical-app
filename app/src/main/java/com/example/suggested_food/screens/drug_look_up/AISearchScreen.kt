package com.example.suggested_food.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AISearchViewModel
import com.example.suggested_food.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISearchScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    viewModel: AISearchViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AISearchViewModel(
                    navController.context.applicationContext as android.app.Application,
                    productViewModel
                ) as T
            }
        }
    )
) {
    var query by remember { mutableStateOf("") }
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tra cứu thuốc AI",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = null, tint = Color.Black)
                    }
                },
                actions = {
                    Icon(Icons.Outlined.SupportAgent, contentDescription = null, tint = Color.Black)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Color(0xFFF5F5F5)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Bạn cần tra cứu thông tin gì ^^") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFFEC4899),
                    ),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.searchDrug(query) }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color(0xFFEC4899))
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (result.isNullOrBlank()) {
                    Text(
                        text = """
                            Chức năng tra cứu thuốc AI giúp bạn tìm hiểu chi tiết các thuốc không kê đơn (OTC) 
                            có trong hệ thống.
                            
                            Lưu ý khi đặt câu hỏi:
                            - Đặt **tên thuốc ở đầu câu**.
                            - Tiếp theo là thắc mắc về thuốc, ví dụ: tác dụng, liều dùng, chống chỉ định...
                            
                            Ví dụ:
                            - "Smecta có tác dụng gì?"
                            - "Paracetamol uống thế nào khi đau đầu?"
                            - "Ibuprofen có tác dụng phụ gì?"
                        """.trimIndent(),
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Justify
                    )
                } else {
                    Text(
                        text = result ?: "",
                        color = Color.DarkGray,
                        textAlign = TextAlign.Justify,
                        fontSize = 14.sp
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5848CE), strokeWidth = 4.dp)
                }
            }
        }
    }
}