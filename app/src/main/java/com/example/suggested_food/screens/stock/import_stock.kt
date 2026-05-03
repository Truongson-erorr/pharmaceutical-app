package com.example.suggested_food.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportStockScreen(
    navController: NavController,
    onSubmit: () -> Unit = {}
) {
    val receiptCode = remember { "PN${System.currentTimeMillis()}" }
    val userName = remember { "Admin" }

    var selectedDate by remember { mutableStateOf(Date()) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var productName by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var lot by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var supplier by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val totalPrice =
        (quantity.toIntOrNull() ?: 0) * (price.toIntOrNull() ?: 0)

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = Date(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Huỷ")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nhập thuốc", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        bottomBar = {
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Xác nhận nhập", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            SectionTitle("Thông tin phiếu")

            FormField("Mã phiếu", receiptCode, {}, "", enabled = false)
            FormField("Người nhập", userName, {}, "", enabled = false)

            Column {
                Text("Ngày nhập", fontWeight = FontWeight.SemiBold)

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(dateFormat.format(selectedDate))

                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Đổi")
                    }
                }
            }

            SectionTitle("Thông tin thuốc")
            FormField("Tên thuốc", productName, { productName = it }, "Nhập tên thuốc")

            FormField("Đơn vị tính", unit, { unit = it }, "Hộp / Vỉ / Viên")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FormField(
                    "Số lượng",
                    quantity,
                    { quantity = it },
                    "0",
                    modifier = Modifier.weight(1f)
                )

                FormField(
                    "Giá nhập",
                    price,
                    { price = it },
                    "0",
                    modifier = Modifier.weight(1f)
                )
            }

            FormField("Số lô", lot, { lot = it }, "Nhập số lô")
            FormField("Hạn sử dụng", expiryDate, { expiryDate = it }, "dd/MM/yyyy")
            FormField("Nhà cung cấp", supplier, { supplier = it }, "Tên nhà cung cấp")
            FormField("Ghi chú", note, { note = it }, "Nhập ghi chú...")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Tổng tiền:",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "$totalPrice",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, fontWeight = FontWeight.Bold, color = Color.Black)
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {

        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint) },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.medium
        )
    }
}