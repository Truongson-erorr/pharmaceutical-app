package com.example.suggested_food.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFeaturesScreen(
    navController: NavController,
) {
    val featureItems = listOf(
        FeatureItem(
            title = "Tra cứu offline",
            desc = "Tìm kiếm thuốc nhanh, không cần internet",
            icon = Icons.Default.Search,
            route = "drug_lookup",
            gradient = listOf(
                Color(0xFF38BDF8),
                Color(0xFF0EA5E9)
            )
        ),

        FeatureItem(
            title = "Gợi ý thuốc",
            desc = "AI đề xuất thuốc phù hợp theo triệu chứng",
            icon = Icons.Outlined.PeopleAlt,
            route = "SuggestScreen",
            gradient = listOf(
                Color(0xFF38BDF8),
                Color(0xFF0EA5E9)
            )
        ),

        FeatureItem(
            title = "Hồ sơ khách hàng",
            desc = "Quản lý thông tin khách hàng và bệnh nhân",
            icon = Icons.Outlined.PeopleAlt,
            route = "PatientScreen",
            gradient = listOf(
                Color(0xFF22C55E),
                Color(0xFF16A34A)
            )
        ),

        FeatureItem(
            title = "Lịch nhắc nhở",
            desc = "Theo dõi và quản lý các lịch hẹn, nhắc nhở",
            icon = Icons.Outlined.NotificationsActive,
            route = "ReminderScreen",
            gradient = listOf(
                Color(0xFFF59E0B),
                Color(0xFFD97706)
            )
        ),

        FeatureItem(
            title = "Quản lý tồn kho",
            desc = "Kiểm soát số lượng và tình trạng thuốc",
            icon = Icons.Outlined.Storefront,
            route = "StockScreen",
            gradient = listOf(
                Color(0xFF8B5CF6),
                Color(0xFF7C3AED)
            )
        ),

        FeatureItem(
            title = "Hóa đơn",
            desc = "Quản lý hóa đơn và lịch sử giao dịch",
            icon = Icons.Outlined.ReceiptLong,
            route = "InvoiceDashboardScreen",
            gradient = listOf(
                Color(0xFFEC4899),
                Color(0xFFDB2777)
            )
        ),

        FeatureItem(
            title = "Nhật ký hoạt động",
            desc = "Theo dõi các thao tác đã thực hiện",
            icon = Icons.Outlined.History,
            route = "ActivityLogScreen",
            gradient = listOf(
                Color(0xFF64748B),
                Color(0xFF475569)
            )
        )
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
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
                            text = "Tất cả tiện ích",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
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
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

            itemsIndexed(featureItems) { index, feature ->
                var visible by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {
                    delay(index * 55L)
                    visible = true
                }

                val alpha by animateFloatAsState(
                    targetValue = if (visible) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = 650,
                        easing = FastOutSlowInEasing
                    ),
                    label = ""
                )

                val translationY by animateFloatAsState(
                    targetValue = if (visible) 0f else 28f,
                    animationSpec = tween(
                        durationMillis = 650,
                        easing = FastOutSlowInEasing
                    ),
                    label = ""
                )

                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            this.alpha = alpha
                            this.translationY = translationY
                        }
                        .fillMaxWidth()
                        .height(190.dp)
                        .clickable {
                            navigateFeature(feature, navController)
                        },
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White,
                                        Color(0xFF2563EB).copy(alpha = 0.03f)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    Brush.linearGradient(
                                        feature.gradient.map {
                                            it.copy(alpha = 0.18f)
                                        }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = feature.icon,
                                contentDescription = null,
                                tint = feature.gradient.first(),
                                modifier = Modifier.size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = feature.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = feature.desc,
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}