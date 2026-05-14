package com.example.suggested_food.screens.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.ProductViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    productViewModel: ProductViewModel = viewModel()
) {
    val product by productViewModel.productDetail.collectAsState()
    val loading by productViewModel.detailLoading.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(productId) {
        productViewModel.fetchProductById(productId)
    }

    val currency = remember {
        NumberFormat.getInstance(Locale("vi", "VN"))
    }

    Scaffold(
        modifier = Modifier.background(Color(0xFFF5F5F5)),
        containerColor = Color.Transparent,

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
                SmallTopAppBar(
                    title = {
                        Text(
                            text = product?.name ?: "Thông tin thuốc",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBackIos,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        }
    ) { innerPadding ->
        when {
            loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không tìm thấy thông tin thuốc")
                }
            }

            else -> {
                val images = product!!.images
                val pagerState = rememberPagerState { images.size }

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                ) {

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) { page ->

                        AsyncImage(
                            model = images[page],
                            contentDescription = product!!.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(images.size) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index)
                                            Color.Black
                                        else Color.LightGray
                                    )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = product!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "${currency.format(product!!.price)} đ",
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⭐ ${product!!.rating}",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = if (product!!.stock > 0)
                                    "Đang lưu hành"
                                else
                                    "Ngưng lưu hành",
                                color = if (product!!.stock > 0)
                                    Color(0xFF16A34A)
                                else Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(Modifier.height(20.dp))

                        Text("Mô tả thuốc", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = product!!.description.ifBlank { "Chưa có mô tả" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Hướng dẫn sử dụng", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            product!!.usage.ifBlank { "Chưa có hướng dẫn sử dụng" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Nhà sản xuất", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(
                            product!!.manufacturer.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Thành phần", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(product!!.ingredients.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Tác dụng phụ", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(product!!.sideEffects.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Liều dùng theo đối tượng", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(product!!.dosageByAge.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Cảnh báo khi sử dụng", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(product!!.warnings.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Dạng bào chế", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(product!!.dosageForm.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Text("Bảo quản", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        Text(product!!.storage.ifBlank { "Chưa có" },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}