package com.example.suggested_food.screens.inventory

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.models.ProductModel
import com.example.suggested_food.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryAddScreen(
    navController: NavController,
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var usage by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var showErrorDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nhập thuốc mới", fontWeight = FontWeight.Bold)
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
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text("Chưa chọn ảnh", color = Color.Gray)
                        }
                    }
                }

                Button(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Text(
                        "Chọn ảnh từ thiết bị",
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.Bold
                    )
                }
                AddField("Tên thuốc", name) { name = it }
                AddField("Mô tả", description) { description = it }
                AddField("Tồn kho", stock) { stock = it }
                AddField("Giá", price) { price = it }
                AddField("Hãng SX", manufacturer) { manufacturer = it }
                AddField("Công dụng", usage) { usage = it }
                AddField("Liều lượng", ingredients) { ingredients = it }
                AddField("HSD", expiryDate) { expiryDate = it }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Button(
                    onClick = {
                        val isInvalid =
                            name.isBlank() ||
                                    description.isBlank() ||
                                    stock.isBlank() ||
                                    price.isBlank() ||
                                    manufacturer.isBlank() ||
                                    usage.isBlank() ||
                                    ingredients.isBlank() ||
                                    expiryDate.isBlank() ||
                                    imageUri == null

                        if (isInvalid) {
                            showErrorDialog = true
                            return@Button
                        }

                        val newProduct = ProductModel(
                            id = "",
                            name = name,
                            description = description,
                            stock = stock.toIntOrNull() ?: 0,
                            price = price.toDoubleOrNull() ?: 0.0,
                            manufacturer = manufacturer,
                            usage = usage,
                            ingredients = ingredients,
                            expiryDate = expiryDate,
                            images = listOf(imageUri.toString())
                        )

                        inventoryViewModel.addProduct(newProduct) {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Thêm thuốc",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    Button(
                        onClick = { showErrorDialog = false }
                    ) {
                        Text("OK")
                    }
                },
                title = {
                    Text("Thiếu thông tin")
                },
                text = {
                    Text("Vui lòng nhập đầy đủ thông tin và chọn ảnh trước khi lưu.")
                }
            )
        }
    }
}

@Composable
fun AddField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {

        Text(
            text = label,
            color = Color.Gray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))

        TextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}