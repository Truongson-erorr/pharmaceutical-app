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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF7C3AED), Color(0xFFEC4899)),
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
                    fontSize = 33.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Tạo tài khoản để bắt đầu sử dụng ứng dụng nhé",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(35.dp)
                ) {

                    Text(
                        "Đăng ký",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Tên người dùng", color = Color.Gray)
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black)
                    )
                    Divider(
                        color = if (name.isNotEmpty()) Color(0xFFEC4899) else Color.LightGray,
                        thickness = 1.5.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Email", color = Color.Gray)
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black)
                    )
                    Divider(
                        color = if (email.isNotEmpty()) Color(0xFFEC4899) else Color.LightGray,
                        thickness = 1.5.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Mật khẩu", color = Color.Gray)
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black)
                    )
                    Divider(
                        color = if (password.isNotEmpty()) Color(0xFFEC4899) else Color.LightGray,
                        thickness = 1.5.dp
                    )
                    Spacer(modifier = Modifier.height(42.dp))

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
                            .height(50.dp),
                        enabled = !loading,
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC4899),
                            contentColor = Color.White
                        )
                    ) {

                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Tạo tài khoản", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (error != null) {
                        Text(
                            text = error ?: "",
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }

                    TextButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Đã có tài khoản? Đăng nhập",
                            color = Color(0xFFEC4899),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}