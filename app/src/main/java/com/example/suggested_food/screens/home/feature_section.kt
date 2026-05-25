package com.example.suggested_food.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                    "Tra cứu offline",
                    Icons.Default.Search,
                    "drug_lookup",
                    listOf(Color(0xFF2563EB), Color(0xFF38BDF8))
                ),
                FeatureItem(
                    "Gợi ý thuốc",
                    Icons.Default.MedicalServices,
                    "SuggestScreen",
                    listOf(Color(0xFF2563EB), Color(0xFF38BDF8))
                ),
            )
        ),
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        featureGroups.forEach { group ->
            Text(
                text = group.title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 12.dp
                ),
                fontSize = 18.sp,
                color = Color(0xFF1E293B),
                letterSpacing = 0.5.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        ((group.items.size + 2) / 3 * 120).dp
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(group.items) { feature ->
                    Card(
                        modifier = Modifier
                            .size(110.dp)
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
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White,
                                            Color(0xFF2563EB).copy(alpha = 0.03f)
                                        )
                                    )
                                )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                colors = feature.gradient
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = feature.icon,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Text(
                                        text = feature.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF0F172A),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = Color(0xFF94A3B8),
                                        modifier = Modifier.size(18.dp)
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

fun navigateFeature(
    feature: FeatureItem,
    navController: NavController
) {
    when (feature.route) {
        "SuggestScreen" -> navController.navigate("SuggestScreen")
        "drug_lookup" -> navController.navigate("drug_lookup")
    }
}