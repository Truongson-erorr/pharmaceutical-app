package com.example.suggested_food.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.models.CartItemModel
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.CartViewModel
import com.example.suggested_food.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    val product by productViewModel.productDetail.collectAsState()
    val loading by productViewModel.detailLoading.collectAsState()
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        productViewModel.fetchProductById(productId)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = product?.name ?: "Chi tiết sản phẩm",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF24006B)
                )
            )
        },

        bottomBar = {
            if (product != null) {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 12.dp,
                                end = 12.dp,
                                top = 8.dp,
                                bottom = 28.dp
                            ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                if (!isLoggedIn) {
                                    Toast.makeText(
                                        context,
                                        "Vui lòng đăng nhập để thêm vào giỏ hàng",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@OutlinedButton
                                }
                                showBottomSheet = true
                            },
                            modifier = Modifier
                                .weight(0.3f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFF24006B))
                        ) {
                            Text(
                                "Giỏ hàng",
                                color = Color(0xFF24006B),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(0.7f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF24006B)
                            )
                        ) {
                            Text(
                                "Mua ngay",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
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
                    CircularProgressIndicator(color = Color(0xFF24006B))
                }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không tìm thấy sản phẩm")
                }
            }

            else -> {

                val images = product!!.images
                val pagerState = rememberPagerState(
                    pageCount = { images.size }
                )

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
                                            Color(0xFF24006B)
                                        else Color.LightGray
                                    )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        Text(
                            text = product!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = formatVND(product!!.price),
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⭐ ${product!!.rating}",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                if (product!!.stock > 0) "Còn hàng" else "Hết hàng",
                                color = if (product!!.stock > 0)
                                    Color(0xFF16A34A) else Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if (product!!.onSale) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "🔥 Đang giảm giá",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(16.dp))

                        Text("Mô tả sản phẩm", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))

                        Text(product!!.description.ifBlank { "Chưa có mô tả" })
                        Spacer(Modifier.height(16.dp))

                        Text(text = "Hướng dẫn sử dụng", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))

                        Text(text = product!!.usage.ifBlank { "Chưa có hướng dẫn sử dụng" }, color = Color.DarkGray)
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White
            ) {
                QuantityBottomSheet(
                    product = product,
                    onConfirm = { quantity ->
                        product?.let {
                            cartViewModel.addToCart(
                                CartItemModel(
                                    productId = it.id,
                                    name = it.name,
                                    image = it.images.firstOrNull() ?: "",
                                    price = it.price,
                                    quantity = quantity
                                )
                            )
                            Toast.makeText(
                                context,
                                "Đã thêm vào giỏ hàng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}

