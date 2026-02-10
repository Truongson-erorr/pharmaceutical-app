package com.example.suggested_food.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
            title = "AI gợi ý thuốc",
            description = "Tư vấn thuốc OTC dựa trên triệu chứng bạn cung cấp",
            imageUrl = "https://cdn-icons-png.flaticon.com/512/8008/8008072.png"
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
            title = "Chat với bác sĩ",
            description = "Trò chuyện trực tiếp với bác sĩ để được tư vấn",
            imageUrl = "https://betapto.edu.vn/upload/2025/06/chibi-bac-si-33.webp"
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
            title = "Tra cứu thuốc AI",
            description = "Tra cứu công dụng, liều dùng và lưu ý an toàn của thuốc",
            imageUrl = "https://media.istockphoto.com/id/1325977231/vi/vec-to/b%E1%BB%99-d%E1%BB%A5ng-c%E1%BB%A5-s%C6%A1-c%E1%BB%A9u-v%E1%BB%9Bi-thu%E1%BB%91c-bi%E1%BB%83u-t%C6%B0%E1%BB%A3ng-vector-h%E1%BB%99p-y-t%E1%BA%BF-v%E1%BA%BD-tay-vali-kh%E1%BA%A9n-c%E1%BA%A5p-d%E1%BB%A5ng-c%E1%BB%A5-b%C3%A1c-s%C4%A9.jpg?s=1024x1024&w=is&k=20&c=HfanywW-4Xts2ibN9dVS3G8zfIyUquNB1tZWRWAw7lo="
        ) {},

        UtilityItem(
            title = "Đơn thuốc của tôi",
            description = "Quản lý và theo dõi các đơn thuốc của bạn",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcj3g5fUD8FZ58y8xeT3dCppkllsztCRBnnkfcHysumHcun7wU6lCY-YGZrdWhisEvE2w&usqp=CAU"
        ) {},

        UtilityItem(
            title = "Tính liều dùng",
            description = "Hỗ trợ tính liều dùng phù hợp theo độ tuổi",
            imageUrl = "https://png.pngtree.com/png-clipart/20250219/original/pngtree-health-syrup-medicine-bottle-icon-clipart-vector-png-image_20166397.png"
        ) {},
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
            .height(180.dp)
            .clickable { item.onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when {
                item.imageUrl != null -> {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(75.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                    )
                }

                item.icon != null -> {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = Color(0xFF1E88E5),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2
            )
        }
    }
}
