package com.example.suggested_food.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suggested_food.viewmodels.AuthViewModel

data class SettingItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val value: String? = null,
    val subtitle: String? = null
)

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel
) {
    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(userName = userName, email = user?.email)

        SectionTitle("Quản lý tài khoản")
        SettingsCard(
            items = listOf(
                SettingItem("Thông tin cá nhân", Icons.Default.Edit),
                SettingItem("Đổi mật khẩu", Icons.Default.Lock),
                SettingItem("Cập nhật số điện thoại", Icons.Default.Email),
                SettingItem("Thêm địa chỉ", Icons.Default.Email),
                SettingItem("Yêu cầu xóa tài khoản", Icons.Default.Email),
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Khác")
        SettingsCard(
            items = listOf(
                SettingItem("Chia sẻ ứng dụng", Icons.Default.Share),
                SettingItem("Phiên bản", Icons.Default.Info, "v1.0.0"),
                SettingItem(
                    title = "Đăng xuất",
                    icon = Icons.Default.Logout,
                )
            ),
            onLogoutClick = { showLogoutDialog = true }
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp),

                title = {
                    Text(
                        text = "Xác nhận đăng xuất",
                        color = Color.Black
                    )
                },

                text = {
                    Text(
                        text = "Bạn có chắc chắn muốn đăng xuất không?",
                        color = Color(0xFF444444)
                    )
                },

                confirmButton = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, bottom = 8.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color(0xFF38BDF8))
                            .clickable {
                                showLogoutDialog = false
                                authViewModel.logout()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Đăng xuất",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text(
                            text = "Huỷ",
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF38BDF8),
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}
