package com.example.suggested_food.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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

@Composable
fun ProfileContent(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()

    val displayName = userName ?: "Người dùng"
    val email = user?.email ?: "Chưa có email"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Spacer(modifier = Modifier.height(28.dp))
        ProfileHeaderUI(
            userName = displayName,
            email = email
        )

        ProfileMenuItem(
            icon = Icons.Outlined.MedicalServices,
            title = "Thuốc của tôi"
        )

        ProfileMenuItem(
            icon = Icons.Outlined.NotificationsActive,
            title = "Nhắc uống thuốc"
        )

        ProfileMenuItem(
            icon = Icons.Outlined.FavoriteBorder,
            title = "Thuốc yêu thích"
        )

        ProfileMenuItem(
            icon = Icons.Outlined.LocationOn,
            title = "Địa chỉ giao hàng",
            onClick = {
                navController.navigate("address")
            }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.HelpOutline,
            title = "Hỗ trợ & trợ giúp"
        )

        ProfileMenuItem(
            icon = Icons.Outlined.Logout,
            title = "Đăng xuất",
            isDanger = true,
            onClick = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo("MainScreen") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun ProfileHeaderUI(
    userName: String,
    email: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = CircleShape,
            color = Color(0xFF08A045),
            modifier = Modifier
                .size(72.dp)
                .offset(y = (-12).dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = userName.firstOrNull()?.uppercase() ?: "U",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    isDanger: Boolean = false,
    onClick: () -> Unit = {}
) {
    val iconColor = if (isDanger) Color.Red else Color(0xFF08A045)
    val iconBgColor = iconColor.copy(alpha = 0.12f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconBgColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = iconColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                color = if (isDanger) Color.Red else Color.Black
            )

            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
