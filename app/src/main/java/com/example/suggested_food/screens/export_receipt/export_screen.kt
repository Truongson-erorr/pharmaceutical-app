package com.example.suggested_food.screens.export_receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.models.ExportReceipt
import com.example.suggested_food.screens.import_receipt.*
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import com.example.suggested_food.viewmodel.ExportViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportStockScreen(
    navController: NavController
) {
    val exportViewModel: ExportViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val products by productViewModel.products.collectAsState()
    val loading by exportViewModel.loading.collectAsState()
    val saveState by exportViewModel.saveState.collectAsState()

    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()

    val receiptCode = remember { "PX${System.currentTimeMillis()}" }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var exportDate by remember { mutableStateOf(Date()) }
    var expiryDate by remember { mutableStateOf(Date()) }

    var showExportPicker by remember { mutableStateOf(false) }
    var showExpiryPicker by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf("") }
    var selectedProductName by remember { mutableStateOf("") }

    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var customer by remember { mutableStateOf("") }
    var lot by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    val totalPrice =
        (quantity.toIntOrNull() ?: 0) *
                (price.toIntOrNull() ?: 0)

    val errorMessage by exportViewModel.errorMessage.collectAsState()
    val backStackEntry = navController.previousBackStackEntry
    val productNameFromDetail = backStackEntry?.savedStateHandle?.get<String>("productName")
    val productIdFromDetail = backStackEntry?.savedStateHandle?.get<String>("productId")

    LaunchedEffect(productNameFromDetail, productIdFromDetail) {
        if (!productNameFromDetail.isNullOrEmpty()) {
            selectedProductName = productNameFromDetail
        }

        if (!productIdFromDetail.isNullOrEmpty()) {
            selectedProductId = productIdFromDetail
        }
    }

    LaunchedEffect(saveState) {
        if (saveState == true) {

        }
    }

    if (showExportPicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showExportPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let {
                            exportDate = Date(it)
                        }
                        showExportPicker = false
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
                DatePicker(state = state)
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
                ) { Text("OK") }
            }
        ) {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF03A9F4),
                    onPrimary = Color.White,
                    surface = Color.White
                )
            ) {
                DatePicker(state = state)
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
                            "Tạo phiếu xuất kho",
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
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2563EB),
                                Color(0xFF38BDF8)
                            )
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable(enabled = !loading) {

                        val receipt = ExportReceipt(
                            id = receiptCode,
                            date = exportDate.time,
                            user = user?.uid ?: "",
                            productId = selectedProductId,
                            productName = selectedProductName,
                            quantity = quantity.toIntOrNull() ?: 0,
                            price = price.toIntOrNull() ?: 0,
                            lot = lot,
                            expiryDate = dateFormat.format(expiryDate),
                            customer = customer,
                            customerPhone = customerPhone,
                            totalPrice = totalPrice
                        )
                        exportViewModel.saveExportReceipt(receipt)
                    },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Xác nhận xuất",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
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
            ReadOnlyField("Người xuất", userName ?: "")

            DateField(
                "Ngày xuất",
                dateFormat.format(exportDate)
            ) { showExportPicker = true }

            SectionTitle("Thông tin thuốc")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                TextField(
                    value = selectedProductName,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Chọn thuốc") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.medium
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

                FormField(
                    "Số lượng",
                    quantity,
                    { quantity = it },
                    "0",
                    Modifier.weight(1f)
                )

                FormField(
                    "Giá bán",
                    price,
                    { price = it },
                    "0",
                    Modifier.weight(1f)
                )
            }
            DateField(
                "Hạn sử dụng",
                dateFormat.format(expiryDate)
            ) {
                showExpiryPicker = true
            }

            FormField("Khách hàng", customer, { customer = it }, "")
            FormField(
                "Số điện thoại",
                customerPhone,
                { customerPhone = it },
                "090xxxxxxx"
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tổng tiền", fontWeight = FontWeight.Bold)
                Text("$totalPrice", fontWeight = FontWeight.Bold)
            }
        }

        if (!errorMessage.isNullOrBlank()) {

            AlertDialog(
                onDismissRequest = {
                    exportViewModel.clearError()
                },
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
                                    color = Color(0xFFFEE2E2),
                                    shape = RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Thất bại",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFFDC2626)
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
                            text = errorMessage ?: "",
                            color = Color.Black
                        )
                    }
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            exportViewModel.clearError()
                        }
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

        if (saveState == true) {

            AlertDialog(
                onDismissRequest = {
                    exportViewModel.clearState()
                },
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
                            text = "Xuất kho thành công!!",
                            color = Color.Black
                        )
                    }
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            exportViewModel.clearState()
                        }
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
