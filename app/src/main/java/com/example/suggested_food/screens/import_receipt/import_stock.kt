package com.example.suggested_food.screens.import_receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.models.ImportReceipt
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import com.example.suggested_food.viewmodel.ImportViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportStockScreen(
    navController: NavController
) {
    val importViewModel: ImportViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val saveState by importViewModel.saveState.collectAsState()
    val loading by importViewModel.loading.collectAsState()

    val products by productViewModel.products.collectAsState()

    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()

    val receiptCode = remember { "PN${System.currentTimeMillis()}" }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var importDate by remember { mutableStateOf(Date()) }
    var expiryDate by remember { mutableStateOf(Date()) }

    var showImportPicker by remember { mutableStateOf(false) }
    var showExpiryPicker by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf("") }
    var selectedProductName by remember { mutableStateOf("") }

    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val lot by remember { mutableStateOf("") }
    var supplier by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val totalPrice =
        (quantity.toIntOrNull() ?: 0) *
                (price.toIntOrNull() ?: 0)

    LaunchedEffect(saveState) {
        if (saveState == true) {

        }
    }

    if (showImportPicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showImportPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let {
                            importDate = Date(it)
                        }
                        showImportPicker = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF03A9F4),
                    onPrimary = Color.White,
                    surface = Color.White,
                    background = Color.White
                )
            ) {
                DatePicker(state)
            }
        }
    }

    if (showExpiryPicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showExpiryPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let {
                            expiryDate = Date(it)
                        }
                        showExpiryPicker = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF03A9F4),
                    onPrimary = Color.White,
                    surface = Color.White,
                    background = Color.White
                )
            ) {
                DatePicker(state)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nhập kho", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },

        bottomBar = {
            Button(
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                onClick = {

                    val receipt = ImportReceipt(
                        id = receiptCode,
                        productId = selectedProductId,
                        user = user?.uid ?: "",
                        date = importDate.time,
                        productName = selectedProductName,
                        quantity = quantity.toIntOrNull() ?: 0,
                        price = price.toIntOrNull() ?: 0,
                        lot = lot,
                        expiryDate = dateFormat.format(expiryDate),
                        supplier = supplier,
                        note = note,
                        totalPrice = totalPrice
                    )
                    importViewModel.saveImportReceipt(receipt)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                if (loading)
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                else
                    Text("Xác nhận nhập")
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F5))
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionTitle("Thông tin phiếu")

            ReadOnlyField("Mã phiếu", receiptCode)
            ReadOnlyField("Người nhập", userName ?: "")

            DateField(
                "Ngày nhập",
                dateFormat.format(importDate)
            ) { showImportPicker = true }
            SectionTitle("Thông tin thuốc")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                InputField(
                    value = selectedProductName,
                    readOnly = true,
                    label = "Chọn thuốc",
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                selectedProductId = product.id
                                selectedProductName = product.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FormField("Số lượng", quantity, { quantity = it }, "0", Modifier.weight(1f))
                FormField("Giá nhập", price, { price = it }, "0", Modifier.weight(1f))
            }

            DateField("Hạn sử dụng", dateFormat.format(expiryDate)) {
                showExpiryPicker = true
            }

            FormField("Nhà cung cấp", supplier, { supplier = it }, "")
            FormField("Ghi chú", note, { note = it }, "")

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tổng tiền", fontWeight = FontWeight.Bold)
                Text("$totalPrice", fontWeight = FontWeight.Bold)
            }
        }

        if (saveState == true) {
            AlertDialog(
                onDismissRequest = { importViewModel.clearState() },
                containerColor = Color.White,

                title = {
                    Text("Thành công")
                },

                text = {
                    Text("Nhập kho thành công")
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            importViewModel.clearState()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, fontWeight = FontWeight.Bold)
}

@Composable
fun ReadOnlyField(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        InputField(value, label, true, Modifier.fillMaxWidth())
    }
}
