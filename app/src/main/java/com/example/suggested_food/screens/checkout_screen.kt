package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.models.CartItemModel
import com.example.suggested_food.viewmodels.CartViewModel
import com.example.suggested_food.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel
) {
    val cartItems by cartViewModel.checkoutItems.collectAsState()

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val shippingFee = 30000.0
    val total = subtotal + shippingFee

    var note by remember { mutableStateOf("") }
    var showQr by remember { mutableStateOf(false) }
    var orderId by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("COD") }
    var showNoteSheet by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val user = userViewModel.user

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SmallTopAppBar(
                title = { Text("Xác nhận đơn hàng", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF24006B)
                )
            )
        },

        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 28.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tổng cộng", color = Color.Gray)
                    Text(
                        formatVND(total),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF24006B)
                    )
                }

                Button(
                    onClick = {
                        isLoading = true

                        cartViewModel.createOrder(
                            cartItems = cartItems,
                            subtotal = subtotal,
                            shippingFee = shippingFee,
                            total = total,
                            note = note,
                            paymentMethod = paymentMethod
                        ) { id ->
                            orderId = id

                            scope.launch {
                                delay(3500)
                                isLoading = false

                                if (paymentMethod == "VIETQR") {
                                    showQr = true
                                } else {
                                    navController.navigate("payment_success") {
                                        popUpTo("checkout") { inclusive = true }
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF24006B)
                    )
                ) {
                    Text("Đặt hàng")
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                AddressSection(
                    user = userViewModel.user,
                    navController = navController
                )
            }

            item {
                Text(
                    "Chi tiết đơn hàng",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(cartItems) {
                CheckoutItemRow(it)
            }

            item {
                NoteRow(
                    note = note,
                    onClick = {
                        showNoteSheet = true
                    }
                )
            }

            item {
                PaymentMethodSelector(
                    selected = paymentMethod,
                    onSelect = { paymentMethod = it }
                )
            }

            item {
                PaymentSummary(subtotal, shippingFee, total)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showQr) {
        ModalBottomSheet(onDismissRequest = {}) {
            val qrUrl = vietQrUrl(amount = total, orderId = orderId)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quét mã để thanh toán", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = qrUrl,
                    contentDescription = null,
                    modifier = Modifier.size(260.dp)
                )

                Text("Số tiền: ${formatVND(total)}")
                Text("Nội dung: $orderId", color = Color.Gray)
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        cartViewModel.markOrderPaid(orderId)
                        showQr = false
                        navController.navigate("payment_success") {
                            popUpTo("checkout") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF24006B)
                    )
                ) {
                    Text("Tôi đã thanh toán")
                }
            }
        }
    }

    if (showNoteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNoteSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    "Lời nhắn cho shop",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = {
                        Text("Ví dụ: Giao giờ hành chính")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF2F2F2),
                        unfocusedContainerColor = Color(0xFFF2F2F2),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showNoteSheet = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF24006B)
                    )
                ) {
                    Text("Xong", color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF24006B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Đang xử lý đơn hàng...",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(
            value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) Color(0xFF24006B) else Color.Black
        )
    }
}

