package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

data class AdminMenu(
    val title: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val name by authViewModel.userName.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf("Dashboard") }

    val menuItems = listOf(
        AdminMenu("Dashboard", Icons.Default.Dashboard),
        AdminMenu("Sản phẩm", Icons.Default.Inventory),
        AdminMenu("Danh mục", Icons.Default.List),
        AdminMenu("Đơn chờ duyệt", Icons.Default.PendingActions),
        AdminMenu("Đang giao", Icons.Default.LocalShipping),
        AdminMenu("Hoàn tất", Icons.Default.TaskAlt),
        AdminMenu("Người dùng", Icons.Default.People),
        AdminMenu("Hỗ trợ trực tuyến", Icons.Default.SupportAgent),
        AdminMenu("Hồ sơ Admin", Icons.Default.AccountCircle)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxWidth(0.7f)) {
                ModalDrawerSheet(
                    drawerContainerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Text(
                            "Admin Panel",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Xin chào $name",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(Modifier.height(12.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(menuItems) { item ->
                            val isSelected = selected == item.title
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selected = item.title
                                        scope.launch { drawerState.close() }
                                    }
                                    .padding(horizontal = 20.dp, vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    item.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFFE53935) else Color(0xFF757575)
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(
                                    item.title,
                                    color = if (isSelected) Color(0xFFE53935) else Color(0xFF757575),
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                authViewModel.logout()
                                navController.navigate("login") { popUpTo(0) }
                            }
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Logout, null, tint = Color(0xFFB71C1C))
                        Spacer(Modifier.width(12.dp))
                        Text("Đăng xuất", color = Color(0xFFB71C1C))
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFF6F7FB),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            selected,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null, tint = Color(0xFFE53935))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            null,
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Nội dung: $selected",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE53935)
                        )
                    }
                }
            }
        }
    }
}