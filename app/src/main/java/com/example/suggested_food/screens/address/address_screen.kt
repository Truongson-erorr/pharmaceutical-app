package com.example.suggested_food.screens.address

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
        containerColor = Color(0xFFE0D9FF),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Địa chỉ giao hàng",
                        fontWeight = FontWeight.SemiBold,
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
                    containerColor = Color(0xFF5848CE)
                )
            )
        },

        bottomBar = {
            Surface(
                color = Color.Transparent,
                tonalElevation = 6.dp
            ) {
                Button(
                    onClick = {
                        userViewModel.updateAddress(address)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5848CE)
                    )
                ) {
                    Text(
                        "Lưu địa chỉ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        "Địa chỉ nhận hàng",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        placeholder = {
                            Text(
                                "Ví dụ: 123 Nguyễn Trãi, Quận 1, TP.HCM",
                                color = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFF1F5F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF5848CE)
                        )
                    )
                }
            }
        }
    }
}



