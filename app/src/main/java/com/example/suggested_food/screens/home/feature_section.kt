package com.example.suggested_food.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel

data class FeatureItem(
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val route: String,
    val gradient: List<Color>
)

data class FeatureGroup(
    val title: String,
    val items: List<FeatureItem>
)

@Composable
fun FeatureSection(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = navController.context

    val featureGroups = listOf(
        FeatureGroup(
            "Tiện ích",
            listOf(
                FeatureItem(
                    title = "Tra cứu offline",
                    desc = "Tìm kiếm thuốc nhanh, không cần internet...",
                    icon = Icons.Default.Search,
                    route = "drug_lookup",
                    gradient = listOf(
                        Color(0xFF38BDF8),
                        Color(0xFF0EA5E9)
                    )
                ),
                FeatureItem(
                    title = "Gợi ý thuốc",
                    desc = "AI đề xuất thuốc phù hợp theo triệu chứng mô tả...",
                    icon = Icons.Default.PeopleAlt,
                    route = "SuggestScreen",
                    gradient = listOf(
                        Color(0xFF38BDF8),
                        Color(0xFF0EA5E9)
                    )
                )
            )
        ),
    )

    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(20.dp))
        featureGroups.forEach { group ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1E293B)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("all_features")
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

            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((group.items.size + 1) / 2 * 190).dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(group.items) { feature ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clickable {
                                if (!isLoggedIn) {
                                    Toast.makeText(
                                        context,
                                        "Vui lòng đăng nhập",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("LoginScreen")
                                    return@clickable
                                }
                                navigateFeature(feature, navController)
                            },
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
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
}

fun navigateFeature(
    feature: FeatureItem,
    navController: NavController
) {
    when (feature.route) {
        "SuggestScreen" -> navController.navigate("SuggestScreen")
        "drug_lookup" -> navController.navigate("drug_lookup")
        "PatientScreen" -> navController.navigate("PatientScreen")
        "ReminderScreen" -> navController.navigate("ReminderScreen")
        "StockScreen" -> navController.navigate("StockScreen")
        "InvoiceDashboardScreen" -> navController.navigate("InvoiceDashboardScreen")
        "ActivityLogScreen" -> navController.navigate("ActivityLogScreen")
    }
}

