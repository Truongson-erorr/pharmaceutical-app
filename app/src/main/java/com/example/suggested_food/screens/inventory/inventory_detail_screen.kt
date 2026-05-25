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
fun InventoryDetailScreen(
    navController: NavController,
    productId: String,
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    val products by inventoryViewModel.products.collectAsState()
    val product = products.find { it.id == productId }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (product == null) {
        Text("Không tìm thấy thuốc")
        return
    }
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
                            "Thông tin thuốc",
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

                if (product.description.isNotBlank()) {
                    InfoBlock("Mô tả", product.description)
                }

                val stockColor =
                    if (product.stock < 10) Color.Red else Color(0xFF1B5E20)

                InfoBlock("Tồn kho", "${product.stock}", stockColor)

                val fields = listOf(
                    "Tên thuốc" to product.name,
                    "Giá" to product.price.toString(),
                    "Hãng SX" to product.manufacturer,
                    "Công dụng" to product.usage,
                    "Liều lượng" to product.ingredients,
                    "HSD" to product.expiryDate,
                    "Giảm giá" to if (product.onSale) "Có" else "Không"
                ).filter { it.second.isNotBlank() }

                fields.forEach { item ->
                    InfoBlock(item.first, item.second)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = {
                        navController.navigate("inventory_edit/${product.id}")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Sửa",
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Xóa",
                        color = Color(0xFFC62828),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = Color.White,

                title = {
                    Text("Xác nhận xóa")
                },
                text = {
                    Text("Bạn có chắc muốn xóa sản phẩm này khỏi kho thuốc không?")
                },
                confirmButton = {
                    Button(
                        onClick = {

                            inventoryViewModel.deleteProduct(
                                product = product,
                                userId = currentUserId,
                                userName = currentUserName
                            ) {
                                showDeleteDialog = false
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC62828)
                        )
                    ) {
                        Text("Xóa", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

@Composable
fun InfoBlock(
    label: String,
    value: String,
    valueColor: Color = Color.Black
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
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            color = valueColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}