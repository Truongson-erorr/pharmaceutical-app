package com.example.suggested_food.authentication

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.R
import com.example.suggested_food.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignIn

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loading by authViewModel.loading.collectAsState()
    val error by authViewModel.error.collectAsState()

    LaunchedEffect(authViewModel.getCurrentUser()) {
        if (authViewModel.getCurrentUser() != null) {
            navController.navigate("MainScreen") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    val context = LocalContext.current

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                authViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF5848CE)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(35.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Pharmaceutical",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Hỗ trợ tra cứu, gợi ý và tìm kiếm thông tin thuốc một cách nhanh chóng ^^",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(35.dp)
                ) {
                    Text(
                        "Đăng nhập",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Email", color = Color.Gray)
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black)
                    )
                    Divider(
                        color = if (email.isNotEmpty()) Color(0xFF5848CE) else Color.LightGray,
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
                        color = if (password.isNotEmpty()) Color(0xFF5848CE) else Color.LightGray,
                        thickness = 1.5.dp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                navController.navigate("ForgotPasswordScreen")
                            }
                        ) {
                            Text(
                                "Quên mật khẩu?",
                                color = Color(0xFF5848CE),
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            authViewModel.login(
                                email.trim(),
                                password
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !loading,
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5848CE),
                            contentColor = Color.White
                        )
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Đăng nhập", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (error != null) {
                        Text(
                            text = error ?: "",
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        Text("  Hoặc  ", color = Color.Gray, fontSize = 12.sp)
                        Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            googleSignInClient.signOut().addOnCompleteListener {
                                launcher.launch(googleSignInClient.signInIntent)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5F5F5),
                            contentColor = Color.Black
                        )
                    ) {
                        AsyncImage(
                            model = "https://developers.google.com/identity/images/g-logo.png",
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            "Đăng nhập bằng Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            navController.navigate("register")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Chưa có tài khoản? Đăng ký",
                            color = Color(0xFF5848CE)
                        )
                    }
                }
            }
        }
    }
}

