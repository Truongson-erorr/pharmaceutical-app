package com.example.suggested_food.screens.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.InventoryViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryEditScreen(
    navController: NavController,
    productId: String,
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    val products by inventoryViewModel.products.collectAsState()
    val product = products.find { it.id == productId }

    if (product == null) {
        Text("Không tìm thấy thuốc")
        return
    }

    var name by remember { mutableStateOf(product.name) }
    var description by remember { mutableStateOf(product.description) }
    var stock by remember { mutableStateOf(product.stock.toString()) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var manufacturer by remember { mutableStateOf(product.manufacturer) }
    var usage by remember { mutableStateOf(product.usage) }
    var ingredients by remember { mutableStateOf(product.ingredients) }
    var expiryDate by remember { mutableStateOf(product.expiryDate) }
    val user = FirebaseAuth.getInstance().currentUser
    val currentUserId = user?.uid ?: "unknown"
    val currentUserName = user?.displayName ?: "Unknown"

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
                            "Cập nhật thông tin thuốc",
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
                AsyncImage(
                    model = product.images.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                )

                EditField("Tên thuốc", name) { name = it }
                EditField("Mô tả", description) { description = it }
                EditField("Tồn kho", stock) { stock = it }
                EditField("Giá", price) { price = it }
                EditField("Hãng SX", manufacturer) { manufacturer = it }
                EditField("Công dụng", usage) { usage = it }
                EditField("Liều lượng", ingredients) { ingredients = it }
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
                        val updated = product.copy(
                            name = name,
                            description = description,
                            stock = stock.toIntOrNull() ?: 0,
                            price = price.toDoubleOrNull() ?: 0.0,
                            manufacturer = manufacturer,
                            usage = usage,
                            ingredients = ingredients,
                            expiryDate = expiryDate
                        )
                        inventoryViewModel.updateProduct(
                            product = updated,
                            userId = currentUserId,
                            userName = currentUserName
                        ) {
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
                        "Lưu thay đổi",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EditField(
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