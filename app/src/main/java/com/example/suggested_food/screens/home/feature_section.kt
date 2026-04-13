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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

data class FeatureItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val color: Color
)

@Composable
fun FeatureSection(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = navController.context

    val features = listOf(
        FeatureItem(
            "Tư vấn bác sĩ",
            Icons.Default.Person,
            "UserChatScreen",
            Color(0xFF007BFF)
        ),
        FeatureItem(
            "Gợi ý thuốc",
            Icons.Default.SmartToy,
            "SymptomRecommendationScreen",
            Color(0xFF10B981)
        ),
        FeatureItem(
            "Tra cứu offline",
            Icons.Default.Search,
            "drug_lookup",
            Color(0xFFF59E0B)
        ),
        FeatureItem(
            "Tra cứu AI",
            Icons.Default.SmartToy,
            "AISearchScreen",
            Color(0xFF6366F1)
        ),
        FeatureItem(
            "Gợi ý AI",
            Icons.Default.AutoAwesome,
            "chat",
            Color(0xFFEF4444)
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(features) { feature ->

            Card(
                modifier = Modifier
                    .size(110.dp)
                    .clickable {
                        if (!isLoggedIn) {
                            Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                            navController.navigate("LoginScreen")
                            return@clickable
                        }

                        when (feature.title) {
                            "Tư vấn bác sĩ" -> {
                                val userId = FirebaseAuth.getInstance().currentUser?.uid
                                if (userId != null) {
                                    navController.navigate("UserChatScreen/$userId")
                                }
                            }
                            else -> navController.navigate(feature.route)
                        }
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(52.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    feature.color.copy(alpha = 0.12f),
                                    CircleShape
                                )
                        )

                        Icon(
                            imageVector = feature.icon,
                            contentDescription = null,
                            tint = feature.color,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = feature.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}