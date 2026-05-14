package com.example.suggested_food.screens.search

import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.ProductViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {

    val products by productViewModel.products.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val pickImageLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            uri ?: return@rememberLauncherForActivityResult

            val inputStream =
                context.contentResolver.openInputStream(uri)

            val bitmap = BitmapFactory.decodeStream(inputStream)
            val image = InputImage.fromBitmap(bitmap, 0)
            val scanner = BarcodeScanning.getClient()

            scanner.process(image)
                .addOnSuccessListener { barcodes ->

                    if (barcodes.isEmpty()) {
                        Log.d("QR_SCAN", "❌ Không tìm thấy QR")
                        return@addOnSuccessListener
                    }

                    val value =
                        barcodes.firstOrNull()?.rawValue
                            ?: return@addOnSuccessListener

                    Log.d("QR_SCAN", "QR VALUE = $value")
                    if (value.startsWith("PRODUCT:")) {

                        val productName =
                            value.removePrefix("PRODUCT:")

                        Log.d("QR_SCAN", "Product name = $productName")

                        val product =
                            products.firstOrNull {
                                it.name.equals(productName, true)
                            }

                        if (product != null) {
                            navController.navigate(
                                "ProductDetail/${product.id}"
                            )
                        } else {
                            Log.d("QR_SCAN", "Không tìm thấy sản phẩm")
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("QR_SCAN", "Scan error", it)
                }
        }

    val suggestions = remember(searchQuery, products) {
        if (searchQuery.isBlank()) emptyList()
        else products
            .filter { it.name.contains(searchQuery, true) }
            .take(7)
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
                            "Tra cứu, tìm kiếm thuốc...",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(16.dp)
        ) {

            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    expanded = it.isNotBlank()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                placeholder = {
                    Text("Nhập tên thuốc hoặc quét QR...")
                },
                singleLine = true,
                trailingIcon = {

                    IconButton(
                        onClick = {
                            pickImageLauncher.launch("image/*")
                        }
                    ) {
                        Icon(Icons.Default.QrCodeScanner, null)
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Gợi ý tìm kiếm thịnh hành",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(Modifier.height(12.dp))

            if (expanded && suggestions.isNotEmpty()) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column {
                        suggestions.forEach { product ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(
                                            "ProductDetail/${product.id}"
                                        )
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                AsyncImage(
                                    model = product.images.firstOrNull(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(Modifier.width(12.dp))

                                Column {
                                    Text(product.name)
                                    Text(
                                        "Xem chi tiết",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}