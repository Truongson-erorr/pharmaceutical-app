package com.example.suggested_food.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val bgTop = Color(0xFF1B1B22)
    val bgBottom = Color(0xFF121218)

    val cardGray = Color(0xFF20202A)
    val textWhite = Color.White
    val textGray = Color.White.copy(alpha = 0.75f)

    val blue = Color(0xFF38BDF8)
    val deepBlue = Color(0xFF2563EB)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(bgTop, bgBottom)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(35.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Pharmaceutical-App",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textWhite
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Hệ thống quản lý nhà thuốc tích hợp AI hỗ trợ tra cứu, quản lý tồn kho, hóa đơn và vận hành hiệu quả.",
                    fontSize = 14.sp,
                    color = textGray
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardGray
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(35.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Bắt đầu sử dụng",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textWhite
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Tạo tài khoản để trải nghiệm đầy đủ các chức năng quản lý nhà thuốc.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.65f),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            navController.navigate("login")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues()
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(deepBlue, blue)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "Bắt đầu trải nghiệm",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))

                                Icon(
                                    imageVector = Icons.Default.NavigateNext,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}