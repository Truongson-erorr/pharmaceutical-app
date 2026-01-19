package com.example.suggested_food.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val user = userViewModel.user
    var address by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    LaunchedEffect(user) {
        address = user?.address ?: ""
    }

    Scaffold(
        containerColor = Color(0xFFF9FAFB),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Địa chỉ giao hàng",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF1E88E5)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Địa chỉ nhận hàng",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        placeholder = {
                            Text("Ví dụ: 123 Nguyễn Trãi, Quận 1, TP.HCM")
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFF1F5F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    userViewModel.updateAddress(address)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5)
                )
            ) {
                Text(
                    "Lưu địa chỉ",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

