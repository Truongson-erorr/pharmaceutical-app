package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems by cartViewModel.checkoutItems.collectAsState()

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val shippingFee = 30000.0
    val total = subtotal + shippingFee

    var showNoteSheet by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("Xác nhận đơn hàng", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF8B0000)
                )
            )
        },

        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tổng cộng", color = Color.Gray)
                    Text(
                        formatVND(total),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B0000)
                    )
                }

                Button(
                    onClick = { },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B0000)
                    ),
                    modifier = Modifier.height(48.dp)
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(1.dp))
            }

            item { AddressSection() }

            item {
                Text("Sản phẩm", fontWeight = FontWeight.Bold)
            }

            items(cartItems) {
                CheckoutItemRow(it)
            }

            item {
                NoteRow(
                    note = note,
                    onClick = { showNoteSheet = true }
                )
            }

            item { ShippingMethod() }
            item { PaymentMethod() }
            item {
                PaymentSummary(subtotal, shippingFee, total)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
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
                    placeholder = { Text("Ví dụ: Giao giờ hành chính") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF2F2F2),
                        unfocusedContainerColor = Color(0xFFF2F2F2),
                        disabledContainerColor = Color(0xFFF2F2F2),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showNoteSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B0000) // cherry
                    )
                ) {
                    Text(
                        "Xong",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun AddressSection() {
        Column {
        Text("Địa chỉ nhận hàng", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Nguyễn Văn A | 0123 456 789")
        Text("123 Nguyễn Trãi, Quận 1, TP.HCM", color = Color.Gray)
    }
}

@Composable
fun CheckoutItemRow(item: CartItemModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.image,
            contentDescription = item.name,
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Medium)
            Text(
                "Số lượng: ${item.quantity}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            formatVND(item.price * item.quantity),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B0000)
        )
    }
}

@Composable
fun NoteRow(
    note: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Lời nhắn cho shop", fontWeight = FontWeight.Bold)
            if (note.isNotEmpty()) {
                Text(note, color = Color.Gray)
            } else {
                Text("Thêm lời nhắn", color = Color.Gray)
            }
        }

        Icon(Icons.Default.KeyboardArrowRight, null)
    }
}

@Composable
fun ShippingMethod() {
    var selected by remember { mutableStateOf("standard") }

    Column {
        Text("Phương thức vận chuyển", fontWeight = FontWeight.Bold)

        ShippingOption("Giao hàng tiêu chuẩn (30.000đ)", selected == "standard") {
            selected = "standard"
        }

        ShippingOption("Giao hàng nhanh", selected == "fast") {
            selected = "fast"
        }
    }
}

@Composable
fun PaymentMethod() {
    var selected by remember { mutableStateOf("cod") }

    Column {
        Text("Phương thức thanh toán", fontWeight = FontWeight.Bold)

        ShippingOption("Thanh toán khi nhận hàng (COD)", selected == "cod") {
            selected = "cod"
        }

        ShippingOption("Chuyển khoản ngân hàng", selected == "bank") {
            selected = "bank"
        }
    }
}

@Composable
fun ShippingOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text)
    }
}

@Composable
fun PaymentSummary(
    subtotal: Double,
    shippingFee: Double,
    total: Double
) {
    Column {
        Text("Chi tiết thanh toán", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        SummaryRow("Tạm tính", formatVND(subtotal))
        SummaryRow("Phí vận chuyển", formatVND(shippingFee))

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        SummaryRow(
            "Tổng cộng",
            formatVND(total),
            isBold = true,
            highlight = true
        )
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
            color = if (highlight) Color(0xFF8B0000) else Color.Black
        )
    }
}
