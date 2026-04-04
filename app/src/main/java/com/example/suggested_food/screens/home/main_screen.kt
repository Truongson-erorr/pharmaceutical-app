package com.example.suggested_food.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    object Home : BottomNavItem("home", Icons.Outlined.Home, Icons.Filled.Home, "Trang chủ")
    object Utilities : BottomNavItem("utilities", Icons.Outlined.Lightbulb, Icons.Filled.Lightbulb, "Tiện ích")
    object Cart : BottomNavItem("cart", Icons.Outlined.ShoppingBag, Icons.Filled.ShoppingBag, "Đơn hàng")
    object Profile : BottomNavItem("profile", Icons.Outlined.Person, Icons.Filled.Person, "Tài khoản")
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
    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (!isSearchActive) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    color = Color(0xFFFF6600)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 20.dp)
                            ) {
                                Spacer(modifier = Modifier.height(30.dp))
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = "Menu",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Nhà thuốc SK",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Row(
                                modifier = Modifier.padding(top = 20.dp)
                            ) {
                                IconButton(
                                    onClick = { isSearchActive = true }
                                ) {
                                    Icon(
                                        Icons.Outlined.Search,
                                        contentDescription = "Search",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { navController.navigate("NotificationsScreen") }
                                ) {
                                    Icon(
                                        Icons.Outlined.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (!isLoggedIn) {
                                            Toast.makeText(context, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show()
                                        } else {
                                            navController.navigate("CartContent")
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Outlined.ShoppingCart,
                                        contentDescription = "Cart",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Chào buổi sáng! ☀️",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Text(
                                    text = if (isLoggedIn) "Chúc bạn một ngày tốt lành" else "Hãy đăng nhập để trải nghiệm tốt hơn",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }

                            if (!isLoggedIn) {
                                Button(
                                    onClick = { navController.navigate("login") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFFFF6600)
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = "Đăng nhập",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp),
                    color = Color(0xFFFF6600)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                isSearchActive = false
                                searchText = ""
                            }
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp)),
                            placeholder = { Text("Tìm kiếm thuốc...", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { searchText = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },

        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    clip = false
                )
            ) {
                bottomItems.forEach { item ->
                    val selected = selectedBottomItem.route == item.route

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) item.filledIcon else item.outlinedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                item.label,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                            )
                        },
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
                            selectedIconColor = Color(0xFFFF6600),
                            selectedTextColor = Color(0xFFFF6600),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFFFF6600).copy(alpha = 0.1f)
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
                BottomNavItem.Home -> HomeContent(navController = navController)
                BottomNavItem.Utilities -> UtilitiesContent(navController, authViewModel)
                BottomNavItem.Cart -> OrderHistoryScreen(navController)
                BottomNavItem.Profile -> ProfileContent(navController, authViewModel)
            }
        }
    }
}
