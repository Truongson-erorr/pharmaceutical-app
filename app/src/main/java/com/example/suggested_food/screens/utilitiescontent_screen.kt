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
            title = "Chat tư vấn với AI",
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
            title = "Đơn thuốc của tôi",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcj3g5fUD8FZ58y8xeT3dCppkllsztCRBnnkfcHysumHcun7wU6lCY-YGZrdWhisEvE2w&usqp=CAU"
        ) {},

        UtilityItem(
            title = "Tính liều dùng",
            icon = Icons.Outlined.Calculate
        ) {},

        UtilityItem(
            title = "Hỗ trợ",
            icon = Icons.Outlined.SupportAgent
        ) {}
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
            .height(140.dp)
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
        }
    }
}
