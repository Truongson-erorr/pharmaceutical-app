package com.example.suggested_food.screens.utilities

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
            "Tư vấn bác sĩ",
            "Kết nối và trò chuyện trực tiếp với bác sĩ để nhận tư vấn sức khỏe cá nhân hóa theo tình trạng của bạn"
        ) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (isLoggedIn && userId != null) {
                navController.navigate("UserChatScreen/$userId")
            } else {
                Toast.makeText(context,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
            }
        },

        UtilityItem(
            "Gợi ý thuốc theo triệu chứng",
            "Mô hình AI được huấn luyện từ dữ liệu y tế phân tích triệu chứng người dùng và đề xuất thuốc OTC phù hợp"
        ) {
            if (isLoggedIn)
                navController.navigate("SymptomRecommendationScreen")
            else
                Toast.makeText(context,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
        },

        UtilityItem(
            "Tra cứu thuốc offline",
            "Tra cứu thông tin thuốc từ cơ sở dữ liệu lớn lưu trữ nội bộ: công dụng, liều dùng, chống chỉ định và cảnh báo an toàn"
        ) {
            if (!isLoggedIn) {
                Toast.makeText(context,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
                navController.navigate("LoginScreen")
            } else {
                navController.navigate("drug_lookup")
            }
        },

        UtilityItem(
            "Tra cứu thuốc AI",
            "Sử dụng AI Gemini phân tích tên thuốc hoặc triệu chứng để cung cấp thông tin và giải thích dễ hiểu"
        ) {
            if (!isLoggedIn) {
                Toast.makeText(context,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
                navController.navigate("LoginScreen")
            } else {
                navController.navigate("AISearchScreen")
            }
        },

        UtilityItem(
            "Gợi ý thuốc AI",
            "Chat AI sử dụng Gemini API để tư vấn nhanh thuốc không kê đơn dựa trên mô tả triệu chứng của bạn"
        ) {
            if (isLoggedIn)
                navController.navigate("chat")
            else
                Toast.makeText(context,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            verticalItemSpacing = 16.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(utilities) { index, item ->
                UtilityCard(item, index)
            }
        }
    }
}

@Composable
fun UtilityCard(item: UtilityItem, index: Int) {

    val gradients = listOf(
        listOf(Color(0xFF3B82F6), Color(0xFF06B6D4)),
        listOf(Color(0xFF7C3AED), Color(0xFFEC4899)),
        listOf(Color(0xFF10B981), Color(0xFF34D399)),
        listOf(Color(0xFF6366F1), Color(0xFFA78BFA)),
        listOf(Color(0xFF0891B2), Color(0xFF22D3EE))
    )
    val gradient = gradients[index % gradients.size]

    val icon = when {
        item.title.contains("bác sĩ") -> Icons.Default.LocalHospital
        item.title.contains("offline") -> Icons.Default.Medication
        item.title.contains("AI") -> Icons.Default.SmartToy
        else -> Icons.Default.HealthAndSafety
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clickable { item.onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(gradient)
                )
                .fillMaxSize()
                .padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        item.description,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = if (item.title.contains("AI"))
                                "🤖 AI Assistant"
                            else
                                "🏥 Healthcare",
                            fontSize = 11.sp,
                            color = Color.White,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }

                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}