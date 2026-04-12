package com.example.suggested_food.screens.utilities

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.models.UtilityItem
import com.example.suggested_food.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UtilitiesContent(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = navController.context

    val utilities = listOf(
        UtilityItem(
            title = "Tư vấn bác sĩ",
            description = "Trò chuyện trực tiếp với bác sĩ để được tư vấn",
        ) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (isLoggedIn && userId != null) {
                navController.navigate("UserChatScreen/$userId") {
                    launchSingleTop = true
                }
            } else {
                Toast.makeText(
                    context,
                    "Vui lòng đăng nhập để được AI tư vấn chi tiết",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },

        UtilityItem(
            title = "Gợi ý thuốc AI",
            description = "Tư vấn thuốc OTC dựa trên triệu chứng bạn cung cấp",
        ) {
            if (isLoggedIn) {
                navController.navigate("chat")
            } else {
                Toast.makeText(
                    context,
                    "Vui lòng đăng nhập để được AI tư vấn chi tiết",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },

        UtilityItem(
            title = "Tra cứu thuốc offline",
            description = "Tra cứu công dụng, liều dùng và lưu ý an toàn của thuốc",
        ) {
            if (!isLoggedIn) {
                Toast.makeText(
                    context,
                    "Vui lòng đăng nhập để sử dụng chức năng này",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate("LoginScreen") {
                    launchSingleTop = true
                }
            } else {
                navController.navigate("drug_lookup")
            }
        },

        UtilityItem(
            title = "Tra cứu thuốc AI",
            description = "Nhập triệu chứng hoặc tên thuốc, nhận gợi ý thông tin và cảnh báo an toàn",
        ) {
            if (!isLoggedIn) {
                Toast.makeText(
                    context,
                    "Vui lòng đăng nhập để sử dụng chức năng AI tra cứu",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate("LoginScreen") {
                    launchSingleTop = true
                }
            } else {
                navController.navigate("AISearchScreen")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            verticalItemSpacing = 16.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            items(utilities) { item ->
                UtilityCard(item)
            }
        }
    }
}

@Composable
fun UtilityCard(item: UtilityItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFFC1B3FF).copy(alpha = 0.3f),
                ambientColor = Color(0xFFC1B3FF).copy(alpha = 0.2f)
            )
            .clickable { item.onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC1B3FF)) // vàng nhạt
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = if (item.title.contains("AI")) "🤖 AI Assistant" else "👨‍⚕️ Medical",
                        fontSize = 10.sp,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1F2937).copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.ChevronRight,
                contentDescription = "Xem chi tiết",
                tint = Color(0xFF1F2937).copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}