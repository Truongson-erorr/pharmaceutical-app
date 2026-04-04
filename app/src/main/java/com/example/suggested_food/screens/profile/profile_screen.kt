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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeaderUI(
            userName = displayName,
            email = email
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0x1A000000),
                    spotColor = Color(0x1A000000)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            )
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Filled.MedicalServices,
                    title = "Hồ sơ sức khỏe",
                    subtitle = "Quản lý thông tin sức khỏe của bạn"
                )

                Divider(
                    color = Color(0xFFEEEEEE),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )

                ProfileMenuItem(
                    icon = Icons.Filled.NotificationsActive,
                    title = "Nhắc uống thuốc",
                    subtitle = "Thiết lập lịch nhắc nhở"
                )

                Divider(
                    color = Color(0xFFEEEEEE),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                ProfileMenuItem(
                    icon = Icons.Filled.Favorite,
                    title = "Thuốc yêu thích",
                    subtitle = "Danh sách thuốc đã lưu"
                )

                Divider(
                    color = Color(0xFFEEEEEE),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                ProfileMenuItem(
                    icon = Icons.Filled.LocationOn,
                    title = "Địa chỉ giao hàng",
                    subtitle = "Quản lý địa chỉ nhận hàng",
                    onClick = {
                        navController.navigate("address")
                    }
                )

                Divider(
                    color = Color(0xFFEEEEEE),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                ProfileMenuItem(
                    icon = Icons.Filled.Help,
                    title = "Hỗ trợ & trợ giúp",
                    subtitle = "Liên hệ và câu hỏi thường gặp"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0x1A000000),
                    spotColor = Color(0x1A000000)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            )
        ) {
            ProfileMenuItem(
                icon = Icons.Filled.Logout,
                title = "Đăng xuất",
                subtitle = "Thoát khỏi tài khoản",
                isDanger = true,
                onClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("MainScreen") { inclusive = true }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileHeaderUI(
    userName: String,
    email: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color(0x33000000),
                        spotColor = Color(0x33000000)
                    )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.firstOrNull()?.uppercase() ?: "U",
                        color = Color(0xFFFF6600),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = Color.Gray.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
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
    val iconColor = if (isDanger) Color.Red else Color(0xFFFF6600)
    val iconBgColor = iconColor.copy(alpha = 0.12f)
    val titleColor = if (isDanger) Color.Red else Color(0xFF333333)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = iconBgColor,
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
}