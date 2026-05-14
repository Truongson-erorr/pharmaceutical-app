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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    .weight(1f)
                    .padding(35.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Đăng ký tài khoản",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textWhite
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Tạo tài khoản để bắt đầu sử dụng ứng dụng nhé",
                    fontSize = 14.sp,
                    color = textGray
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
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
                        "Đăng ký",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textWhite
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Tên người dùng", color = Color(0xFF9CA3AF))
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = textWhite)
                    )
                    Divider(
                        color = if (name.isNotEmpty())
                            blue
                        else
                            Color(0xFF3A3A45),
                        thickness = 1.5.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

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
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Mật khẩu", color = Color(0xFF9CA3AF))
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = textWhite)
                    )
                    Divider(
                        color = if (password.isNotEmpty())
                            blue
                        else
                            Color(0xFF3A3A45),
                        thickness = 1.5.dp
                    )
                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            authViewModel.register(
                                email.trim(),
                                password,
                                name.trim(),
                                "user"
                            )
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
                                    "Tạo tài khoản",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
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

                    TextButton(
                        onClick = {
                            navController.navigate("login")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Đã có tài khoản? Đăng nhập",
                            color = blue
                        )
                    }
                }
            }
        }
    }
}