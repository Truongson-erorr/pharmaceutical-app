package com.example.suggested_food.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.suggested_food.viewmodels.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    navController: NavController,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val categories by categoryViewModel.categories.collectAsState()
    val loading by categoryViewModel.loading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Xin chào",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Trường Sơn",
                    color = Color(0xFF8B0000),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            IconButton(onClick = { navController.navigate("SearchScreen") }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.Black
                )
            }

            IconButton(onClick = { navController.navigate("NotificationsScreen") }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Thông báo",
                    tint = Color.Black
                )
            }

            IconButton(onClick = { navController.navigate("CartContent") }) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Giỏ hàng",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        BannerSlider()

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Danh mục",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Xem tất cả",
                color = Color(0xFF8B0000),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("AllCategoriesScreen")
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (loading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFF8B0000),
                    strokeWidth = 2.dp
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(categories) { category ->
                    CategoryHorizontalItem(
                        name = category.name,
                        imageUrl = category.imageUrl
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sản phẩm bán chạy",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Xem tất cả",
                color = Color(0xFF8B0000),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
