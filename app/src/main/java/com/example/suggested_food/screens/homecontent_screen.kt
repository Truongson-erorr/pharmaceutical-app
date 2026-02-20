package com.example.suggested_food.screens

import android.os.Build
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
    authViewModel: AuthViewModel
) {
    val categories by categoryViewModel.categories.collectAsState()
    val loading by categoryViewModel.loading.collectAsState()

    val products by productViewModel.products.collectAsState()
    val productLoading by productViewModel.loading.collectAsState()

    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val userName by authViewModel.userName.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, null, tint = Color.Gray)
                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = greetingByTime(),
                        fontSize = 15.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = if (isLoggedIn && !userName.isNullOrBlank())
                            userName!!
                        else
                            "Quý khách",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF24006B)
                    )
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            BannerSlider()
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Danh mục", fontWeight = FontWeight.Bold)
                Text(
                    "Xem tất cả",
                    color = Color(0xFF24006B),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("AllCategoriesScreen")
                    }
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            if (loading) {
                CircularProgressIndicator(color = Color(0xFF24006B))
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sản phẩm phổ biến",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = "Lọc",
                        tint = Color.Black
                    )
                }
            }
        }

        if (productLoading) {
            item(span = { GridItemSpan(2) }) {
                CircularProgressIndicator(color = Color(0xFF24006B))
            }
        } else {
            items(products) { product ->
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



