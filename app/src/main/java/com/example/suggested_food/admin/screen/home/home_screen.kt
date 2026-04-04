package com.example.suggested_food.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.admin.screen.home.DashboardScreen
import com.example.suggested_food.models.AdminMenu
import com.example.suggested_food.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import com.example.suggested_food.admin.screen.product.ProductScreen
import com.example.suggested_food.admin.screen.order.OrderScreen
import com.example.suggested_food.admin.screen.order.CompletedScreen
import com.example.suggested_food.admin.screen.order.DeliveringScreen
import com.example.suggested_food.admin.screen.category.CategoryScreen
import com.example.suggested_food.admin.screen.support.SupportScreen
import com.example.suggested_food.admin.screen.user.UsersScreen
import com.example.suggested_food.admin.screen.profile.AdminProfileScreen

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

    val primaryColor = Color(0xFF08A045)

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
                                    colors = listOf(primaryColor, primaryColor)
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
                                    tint = if (isSelected) primaryColor else Color(0xFF757575)
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(
                                    item.title,
                                    color = if (isSelected) primaryColor else Color(0xFF757575),
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
                        Icon(Icons.Default.Logout, null, tint = Color.Red)
                        Spacer(Modifier.width(12.dp))
                        Text("Đăng xuất", color = Color.Red, fontWeight = FontWeight.Bold)
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
                            color = primaryColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null, tint = primaryColor)
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
                when (selected) {
                    "Dashboard" -> DashboardScreen()
                    "Sản phẩm" -> ProductScreen()
                    "Danh mục" -> CategoryScreen()
                    "Đơn chờ duyệt" -> OrderScreen()
                    "Đang giao" -> DeliveringScreen()
                    "Hoàn tất" -> CompletedScreen()
                    "Người dùng" -> UsersScreen()
                    "Hỗ trợ trực tuyến" -> SupportScreen()
                    "Hồ sơ Admin" -> AdminProfileScreen()
                    else -> DashboardScreen()
                }
            }
        }
    }
}