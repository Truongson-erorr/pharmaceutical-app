package com.example.suggested_food.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    navController: NavController,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    val selectedItems = cartItems.filter { selectedIds.contains(it.productId) }
    val selectedTotal = selectedItems.sumOf { it.price * it.quantity }

    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            cartViewModel.loadCartFromFirestore()
        } else {
            navController.navigate("login") {
                popUpTo("CartContent") { inclusive = true }
            }
        }
    }

    if (!isLoggedIn) return

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("Giỏ hàng", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF00246B)
                )
            )
        },

        bottomBar = {
            if (selectedItems.isNotEmpty()) {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tổng tiền", color = Color.Gray)
                            Text(
                                text = formatVND(selectedTotal),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00246B)
                            )
                        }

                        Button(
                            onClick = {
                                cartViewModel.setCheckoutItems(selectedItems)
                                navController.navigate("checkout")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00246B)
                            )
                        ) {
                            Text("Mua hàng")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Giỏ hàng trống",
                        tint = Color(0xFF00246B),
                        modifier = Modifier.size(66.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Giỏ hàng của bạn đang trống",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Hãy thêm thuốc để tiếp tục",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    items = cartItems,
                    key = { it.productId }
                ) { item ->
                    CartItemRow(
                        item = item,
                        checked = selectedIds.contains(item.productId),
                        onCheckedChange = { checked ->
                            selectedIds = if (checked)
                                selectedIds + item.productId
                            else
                                selectedIds - item.productId
                        },
                        onIncrease = {
                            cartViewModel.updateQuantity(
                                item.productId,
                                item.quantity + 1
                            )
                        },
                        onDecrease = {
                            if (item.quantity > 1) {
                                cartViewModel.updateQuantity(
                                    item.productId,
                                    item.quantity - 1
                                )
                            }
                        },
                        onRemove = {
                            cartViewModel.removeItem(item.productId)
                            selectedIds = selectedIds - item.productId
                        },
                        onClick = {
                            navController.navigate("ProductDetail/${item.productId}")
                        }
                    )
                    Divider()
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

        }
    }
}
