package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.DrugLookupViewModel
import com.example.suggested_food.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugLookupScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
) {
    var query by remember { mutableStateOf("") }

    val viewModel: DrugLookupViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DrugLookupViewModel(
                    application = navController.context.applicationContext as android.app.Application,
                    productViewModel = productViewModel
                ) as T
            }
        }
    )

    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tra cứu thuốc AI", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    Icon(Icons.Outlined.SupportAgent, contentDescription = null, tint = Color.White, modifier = Modifier.padding(end = 16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF24006B))
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = query,
                    onValueChange = { query  = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(14.dp)),
                    placeholder = {
                        Text(
                            "Bạn cần tra cứu thông tin gì ^^",
                            color = Color(0xFF6B7280)
                        )
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF6B7280)
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3F4F6),   // light gray
                        unfocusedContainerColor = Color(0xFFF3F4F6),
                        disabledContainerColor = Color(0xFFF3F4F6),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

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
                        text = result!!,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = Color.DarkGray
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF24006B), strokeWidth = 4.dp)
                }
            }
        }
    }
}