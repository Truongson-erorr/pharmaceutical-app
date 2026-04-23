package com.example.suggested_food.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.screens.product.ProductGridItem
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.CategoryViewModel
import com.example.suggested_food.viewmodels.ProductViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    navController: NavController,
    categoryViewModel: CategoryViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val categories by categoryViewModel.categories.collectAsState()
    val loading by categoryViewModel.loading.collectAsState()

    val products by productViewModel.products.collectAsState()
    val productLoading by productViewModel.loading.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF5F5F5)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        navController.navigate("SearchScreen")
                    },
                shape = RoundedCornerShape(14.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nhập tên thuốc...",
                        color = Color(0xFF6B7280),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF6B7280)
                    )
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            FeatureSection(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Danh mục",
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("AllCategoriesScreen")
                    }
                ) {
                    Text(
                        "Xem tất cả",
                        color = Color(0xFFEC4899),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFEC4899),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            if (loading) {
                CircularProgressIndicator(color = Color(0xFFEC4899))
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories) { category ->
                        CategoryHorizontalItem(
                            name = category.name,
                            imageUrl = category.imageUrl,
                            onClick = {
                                navController.navigate("category/${category.id}")
                            }
                        )
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Sản phẩm phổ biến",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("AllProductScreen")
                    }
                ) {
                    Text(
                        text = "Xem tất cả",
                        color = Color(0xFFEC4899),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFEC4899),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        if (productLoading) {
            item(span = { GridItemSpan(2) }) {
                CircularProgressIndicator(color = Color(0xFFEC4899))
            }
        } else {
            items(products.take(6)) { product ->
                ProductGridItem(
                    product = product,
                    onClick = {
                        navController.navigate("ProductDetail/${product.id}")
                    }
                )
            }
        }
    }
}