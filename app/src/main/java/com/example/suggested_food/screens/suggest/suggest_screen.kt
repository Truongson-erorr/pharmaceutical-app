package com.example.suggested_food.screens.suggest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suggested_food.viewmodels.SuggestViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SuggestScreen(
    navController: NavController,
    viewModel: SuggestViewModel = viewModel()
) {
    val symptom = remember { mutableStateOf("") }

    val result by viewModel.result.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val commonSymptoms = listOf(
        "Sổ mũi",
        "Nghẹt mũi",
        "Đau họng",
        "Sốt",
        "Ho",
        "Đau đầu",
        "Chóng mặt",
        "Mệt mỏi",
        "Đau bụng",
        "Buồn nôn",
        "Tiêu chảy",
        "Đau lưng",
        "Đau cơ",
        "Mất ngủ",
        "Khó thở",
        "Đau ngực"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "AI gợi ý thuốc",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, null, tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())   // 🔥 CHỈ THÊM DÒNG NÀY
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = symptom.value,
                    onValueChange = { symptom.value = it },
                    placeholder = { Text("Bạn đang cảm thấy thế nào?") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, null)
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        viewModel.suggest(symptom.value)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF7C3AED), Color(0xFFEC4899))
                                ),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "AI",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            if (loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (!result.isNullOrEmpty()) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            "Top thuốc gợi ý",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        result!!.forEachIndexed { index, item ->

                            Text(
                                text = "${index + 1}. $item",
                                color = Color(0xFF7C3AED),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Text(
                "Triệu chứng thường gặp",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                commonSymptoms.forEach { item ->

                    AssistChip(
                        onClick = {
                            symptom.value = item
                        },
                        label = {
                            Text(
                                text = item,
                                color = Color(0xFFEC4899),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFFFE4EC)
                        ),
                        border = null
                    )
                }
            }
        }
    }
}