package com.example.suggested_food.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.screens.order.OrderHistoryScreen
import com.example.suggested_food.screens.profile.ProfileContent
import com.example.suggested_food.screens.utilities.UtilitiesContent
import com.example.suggested_food.viewmodels.AuthViewModel

sealed class BottomNavItem(
    val route: String,
    val outlinedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val filledIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Outlined.HomeWork, Icons.Filled.HomeWork, "Trang chủ")
    object Utilities : BottomNavItem("utilities", Icons.Outlined.Lightbulb, Icons.Filled.Lightbulb, "Tiện ích")
    object Cart : BottomNavItem("cart", Icons.Outlined.LocalShipping, Icons.Filled.LocalShipping, "Theo dõi đơn")
    object Profile : BottomNavItem("profile", Icons.Outlined.Person, Icons.Filled.Person, "Tôi")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.Utilities, BottomNavItem.Cart, BottomNavItem.Profile)
    var selectedBottomItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Column {
                SmallTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp).clickable {
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Nhà thuốc SK",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("NotificationsScreen")
                        }) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.White)
                        }
                        IconButton(onClick = {
                            if (!isLoggedIn) {
                                Toast.makeText(context, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show()
                            } else {
                                navController.navigate("CartContent")
                            }
                        }) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF08A045))
                )
            }
        },

        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 2.dp
            ) {
                bottomItems.forEach { item ->
                    val selected = selectedBottomItem.route == item.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) item.filledIcon else item.outlinedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            when (item) {
                                BottomNavItem.Profile -> {
                                    if (!isLoggedIn) {
                                        navController.navigate("login") { launchSingleTop = true }
                                    } else selectedBottomItem = item
                                }
                                BottomNavItem.Cart -> {
                                    if (!isLoggedIn) {
                                        Toast.makeText(context, "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show()
                                    } else selectedBottomItem = item
                                }
                                else -> selectedBottomItem = item
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF08A045),
                            selectedTextColor = Color(0xFF08A045),
                            unselectedIconColor = Color.Gray.copy(alpha = 0.7f),
                            unselectedTextColor = Color.Gray.copy(alpha = 0.7f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedBottomItem) {
                BottomNavItem.Home -> HomeContent(navController = navController)
                BottomNavItem.Utilities -> UtilitiesContent(navController, authViewModel)
                BottomNavItem.Cart -> OrderHistoryScreen(navController)
                BottomNavItem.Profile -> ProfileContent(navController, authViewModel)
            }
        }
    }
}