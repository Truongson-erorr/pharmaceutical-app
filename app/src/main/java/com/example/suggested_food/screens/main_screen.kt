package com.example.suggested_food.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

sealed class BottomNavItem(
    val route: String,
    val outlinedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val filledIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
) {
    object Home : BottomNavItem(
        "home",
        Icons.Outlined.HomeWork,
        Icons.Filled.HomeWork,
        "Trang chủ"
    )

    object Utilities : BottomNavItem(
        "utilities",
        Icons.Outlined.Lightbulb,
        Icons.Filled.Lightbulb,
        "Tiện ích"
    )

    object Cart : BottomNavItem(
        "cart",
        Icons.Outlined.LocalShipping,
        Icons.Filled.LocalShipping,
        "Theo dõi đơn"
    )

    object Profile : BottomNavItem(
        "profile",
        Icons.Outlined.Person,
        Icons.Filled.Person,
        "Tôi"
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Utilities,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    var selectedBottomItem by remember {
        mutableStateOf<BottomNavItem>(BottomNavItem.Home)
    }

    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFCADCFC),

        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = "Nhà thuốc SK",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {

                    IconButton(
                        onClick = {
                            navController.navigate("SearchScreen")
                        }
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            navController.navigate("NotificationsScreen")
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            if (!isLoggedIn) {
                                Toast.makeText(
                                    context,
                                    "Vui lòng đăng nhập để xem giỏ hàng",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                navController.navigate("CartContent")
                            }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF24006B)
                )
            )
        },

        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF24006B),
                tonalElevation = 0.dp,
                modifier = Modifier
                    .height(95.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
            ) {
                bottomItems.forEach { item ->

                    val selected = selectedBottomItem.route == item.route

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector =
                                if (selected)
                                    item.filledIcon
                                else
                                    item.outlinedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            when (item) {
                                BottomNavItem.Profile -> {
                                    if (!isLoggedIn) {
                                        navController.navigate("login") {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        selectedBottomItem = item
                                    }
                                }
                                BottomNavItem.Cart -> {
                                    if (!isLoggedIn) {
                                        Toast.makeText(
                                            context,
                                            "Vui lòng đăng nhập để sử dụng chức năng này",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        selectedBottomItem = item
                                    }
                                }

                                else -> {
                                    selectedBottomItem = item
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedBottomItem) {
                BottomNavItem.Home ->
                    HomeContent(
                        navController = navController,
                        authViewModel = authViewModel
                    )

                BottomNavItem.Utilities ->
                    UtilitiesContent(navController, authViewModel)

                BottomNavItem.Cart ->
                    OrderHistoryScreen(navController)

                BottomNavItem.Profile ->
                    ProfileContent(navController, authViewModel)
            }
        }
    }
}
