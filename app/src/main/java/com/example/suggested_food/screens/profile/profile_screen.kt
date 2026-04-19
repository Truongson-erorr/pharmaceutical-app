package com.example.suggested_food.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            .background(Color(0xFFF5F5F5))
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Tài khoản",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBackIos,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            ProfileHeaderUI(displayName, email)
            Spacer(Modifier.height(16.dp))

            MenuGroup(title = "Sức khỏe") {
                ProfileMenuItem(
                    icon = Icons.Default.MedicalServices,
                    title = "Hồ sơ sức khỏe",
                    subtitle = "Quản lý thông tin sức khỏe",
                    onClick = { navController.navigate("health_profile") }
                )
                ProfileMenuItem(
                    icon = Icons.Default.NotificationsActive,
                    title = "Nhắc uống thuốc",
                    subtitle = "Lịch nhắc nhở thuốc"
                )
            }
            Spacer(Modifier.height(16.dp))

            MenuGroup(title = "Tiện ích") {
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Địa chỉ giao hàng",
                    subtitle = "Quản lý địa chỉ",
                    onClick = { navController.navigate("address") }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Help,
                    title = "Hỗ trợ",
                    subtitle = "FAQ & liên hệ"
                )
            }
            Spacer(Modifier.height(16.dp))

            MenuGroup(title = "Tài khoản") {
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    title = "Đăng xuất",
                    subtitle = "Thoát tài khoản",
                    isDanger = true,
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("MainScreen") { inclusive = true }
                        }
                    }
                )
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun MenuGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))

        Column {
            content()
        }

    }
}

@Composable
fun ProfileHeaderUI(
    userName: String,
    email: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFF007BFF).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF007BFF),
                modifier = Modifier.size(45.dp)
            )
        }
        Spacer(Modifier.height(12.dp))

        Text(
            text = userName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = email,
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String = "",
    isDanger: Boolean = false,
    onClick: () -> Unit = {}
) {
    val color = if (isDanger) Color.Red else Color(0xFF007BFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(color.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}