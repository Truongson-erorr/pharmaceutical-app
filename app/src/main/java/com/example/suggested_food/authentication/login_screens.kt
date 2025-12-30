package com.example.suggested_food.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Đăng nhập",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Đăng nhập")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (error != null) {
            Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
        }

        TextButton(
            onClick = {
                navController.navigate("register")
            }
        ) {
            Text("Chưa có tài khoản? Đăng ký")
        }
    }
}


