package com.example.suggested_food.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }

    val loading by authViewModel.loading.collectAsState()
    val error by authViewModel.error.collectAsState()

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
                    .weight(2f)
                    .padding(35.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Quên mật khẩu",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textWhite
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Nhập email để nhận liên kết đặt lại mật khẩu",
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
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(35.dp)
                ) {

                    Text(
                        "Đặt lại mật khẩu",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textWhite
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Email", color = Color(0xFF9CA3AF))
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = textWhite)
                    )

                    Divider(
                        color = if (email.isNotEmpty())
                            blue
                        else
                            Color(0xFF3A3A45),
                        thickness = 1.5.dp
                    )
                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            authViewModel.resetPassword(email.trim())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !loading,
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

                            if (loading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Gửi email đặt lại mật khẩu",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (error != null) {
                        Text(
                            text = error ?: "",
                            color = Color(0xFFEF4444),
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Quay lại",
                            color = blue
                        )
                    }
                }
            }
        }
    }
}