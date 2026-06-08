package com.example.suggested_food.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
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

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(80)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,

        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

    val translationY by animateFloatAsState(
        targetValue = if (visible) 0f else 35f,

        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .graphicsLayer {
                this.alpha = alpha
                this.translationY = translationY
            }
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
                        text = "Nhập tên thuốc để tìm kiếm...",
                        color = Color.DarkGray,
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
            Column {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Phân loại điều trị",
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
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            if (loading) {
                CircularProgressIndicator(color = Color(0xFF38BDF8))
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
            Column {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Thư viện thuốc",
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
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        if (productLoading) {
            item(span = { GridItemSpan(2) }) {
                CircularProgressIndicator(color = Color(0xFF38BDF8))
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