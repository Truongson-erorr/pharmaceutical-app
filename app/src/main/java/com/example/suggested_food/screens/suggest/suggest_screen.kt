package com.example.suggested_food.screens.suggest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.SuggestViewModel
import com.valentinilk.shimmer.shimmer

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
        "Sổ mũi","Nghẹt mũi","Đau họng",
        "Đau đầu","Chóng mặt","Mệt mỏi","Đau bụng",
        "Buồn nôn","Đau lưng","Đau cơ",
        "Khó thở","Đau ngực"
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2563EB), Color(0xFF38BDF8))
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Gợi ý thuốc - AI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Box(Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = symptom.value,
                    onValueChange = { symptom.value = it },
                    placeholder = { Text("Bạn đang cảm thấy thế nào?") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
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
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF2563EB),
                                        Color(0xFF38BDF8)
                                    )
                                ),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("AI", color = Color.White)
                        }
                    }
                }
            }

            if (loading) {

                Text(
                    "Đang phân tích triệu chứng...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                repeat(3) {
                    ShimmerDrugCard()
                }
            }

            if (!loading && result.isNotEmpty()) {

                Text(
                    "Top 10 thuốc gợi ý",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                result.forEachIndexed { index, drug ->

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Text(
                                "${index + 1}. ${drug.name}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )

                            if (drug.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = drug.imageUrl,
                                    contentDescription = drug.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }

                            Text("Thành phần:", fontWeight = FontWeight.Bold)
                            Text(drug.composition)

                            Text("Công dụng:", fontWeight = FontWeight.Bold)
                            Text(drug.uses)

                            Text("Tác dụng phụ:", fontWeight = FontWeight.Bold)
                            Text(drug.sideEffects)

                            Text("Nhà sản xuất:", fontWeight = FontWeight.Bold)
                            Text(drug.manufacturer)

                            Text("Đánh giá từ người dùng", fontWeight = FontWeight.Bold)

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Xuất sắc: ${drug.excellent}%")
                                Text("Trung bình: ${drug.average}%")
                                Text("Kém: ${drug.poor}%")
                            }
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
                commonSymptoms.forEach {
                    AssistChip(
                        onClick = { symptom.value = it },
                        label = { Text(it, color = Color(0xFF38BDF8)) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE0F2FE)
                        ),
                        border = null
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerDrugCard() {

    val shimmerColor = Color.Gray
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerColor)
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(shimmerColor)
            )

            repeat(5) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(shimmerColor)
                )
            }
        }
    }
}