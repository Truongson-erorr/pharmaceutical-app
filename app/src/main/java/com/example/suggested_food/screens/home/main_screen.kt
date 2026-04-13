package com.example.suggested_food.screens.home

import android.os.Build
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.screens.order.OrderHistoryScreen
import com.example.suggested_food.screens.profile.ProfileContent
import com.example.suggested_food.screens.utilities.UtilitiesContent
import com.example.suggested_food.viewmodels.AuthViewModel

sealed class BottomNavItem(
    val route: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val label: String
) {
    object Home :
        BottomNavItem("home", Icons.Outlined.Home, Icons.Filled.Home, "Trang chủ")

    object Utilities :
        BottomNavItem("utilities", Icons.Outlined.AutoAwesome, Icons.Filled.Lightbulb, "Tiện ích")

    object Cart :
        BottomNavItem("cart", Icons.Outlined.ShoppingBag, Icons.Filled.ShoppingBag, "Đơn hàng")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Utilities,
        BottomNavItem.Cart,
    )

    var selectedBottomItem by remember {
        mutableStateOf<BottomNavItem>(BottomNavItem.Home)
    }
    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (selectedBottomItem == BottomNavItem.Home) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF007BFF),
                                    Color(0xFF00C2FF)
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 40.dp,
                                bottom = 20.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hello, Son",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 34.sp,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Welcome back 👋",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.navigate("NotificationsScreen") }) {
                                Icon(
                                    Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }

                            IconButton(onClick = {
                                if (!isLoggedIn) {
                                    Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                                } else {
                                    navController.navigate("CartContent")
                                }
                            }) {
                                Icon(
                                    Icons.Outlined.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White
                                )
                            }

                            IconButton(onClick = {
                                navController.navigate("ProfileContent")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },

        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                bottomItems.forEach { item ->

                    val selected =
                        selectedBottomItem.route == item.route

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector =
                                if (selected) item.filledIcon
                                else item.outlinedIcon,
                                contentDescription = item.label
                            )
                        },

                        label = {
                            Text(
                                item.label,
                                fontSize = 12.sp,
                                fontWeight =
                                if (selected)
                                    FontWeight.Medium
                                else FontWeight.Normal
                            )
                        },
                        selected = selected,

                        onClick = {
                            when (item) {
                                BottomNavItem.Cart -> {
                                    if (!isLoggedIn) {
                                        Toast.makeText(
                                            context,
                                            "Vui lòng đăng nhập",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else selectedBottomItem = item
                                }

                                else -> selectedBottomItem = item
                            }
                        },

                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF007BFF),
                            selectedTextColor = Color(0xFF007BFF),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor =
                            Color(0xFF007BFF).copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            when (selectedBottomItem) {
                BottomNavItem.Home ->
                    HomeContent(navController)
                BottomNavItem.Utilities ->
                    UtilitiesContent(navController, authViewModel)
                BottomNavItem.Cart ->
                    OrderHistoryScreen(navController)
            }
        }
    }
}
