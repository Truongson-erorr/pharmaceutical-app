package com.example.suggested_food.screens.import_receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF2563EB),
                                Color(0xFF38BDF8)
                            )
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Tạo phiếu nhập kho",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(45.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2563EB),
                                Color(0xFF38BDF8)
                            )
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable(enabled = !loading) {
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
                contentAlignment = Alignment.Center
            ) {

                if (loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Xác nhận nhập",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
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

                title = @androidx.compose.runtime.Composable {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .background(
                                    color = Color(0xFFD1FAE5),
                                    shape = RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF22C55E),
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Success",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    }
                },

                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nhập kho thành công!!",
                            color = Color.Black
                        )
                    }
                },

                confirmButton = {
                    TextButton(
                        onClick = { importViewModel.clearState() }
                    ) {
                        Text(
                            "OK",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
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
