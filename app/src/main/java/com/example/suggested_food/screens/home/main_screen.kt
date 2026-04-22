package com.example.suggested_food.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.suggested_food.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

sealed class BottomNavItem(
    val route: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val label: String
) {
    object Home :
        BottomNavItem("home", Icons.Outlined.Home, Icons.Filled.Home, "Trang chủ")

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
        BottomNavItem.Cart
    )
    var selectedBottomItem by remember {
        mutableStateOf<BottomNavItem>(BottomNavItem.Home)
    }

    val isLoggedIn by authViewModel.isLoggedInFlow.collectAsState()
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()

    val displayName = userName ?: "Người dùng"
    val email = user?.email ?: "Chưa có email"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF7C3AED), Color(0xFFEC4899)),
                            )
                        )
                        .padding(top = 50.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFFEC4899),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = displayName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = email,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                @Composable
                fun DrawerItem(
                    title: String,
                    icon: ImageVector,
                    onClick: () -> Unit
                ) {
                    NavigationDrawerItem(
                        selected = false,
                        onClick = onClick,
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )
                }

                DrawerItem(
                    "Hồ sơ sức khỏe",
                    Icons.Default.Person
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("ProfileContent")
                }

                DrawerItem(
                    "Kiểm tra tương tác thuốc",
                    Icons.Default.Medication
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("DrugInteractionScreen")
                }

                DrawerItem(
                    "Lịch sử dùng thuốc",
                    Icons.Default.History
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("MedicineHistoryScreen")
                }

                DrawerItem(
                    "Nhắc uống thuốc",
                    Icons.Default.Alarm
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("ReminderScreen")
                }

                DrawerItem(
                    "Đơn thuốc của tôi",
                    Icons.Default.Description
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("PrescriptionScreen")
                }

                DrawerItem(
                    "Chỉ số sức khỏe",
                    Icons.Default.Favorite
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("HealthIndexScreen")
                }

                DrawerItem(
                    "Triệu chứng hôm nay",
                    Icons.Default.MonitorHeart
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("SymptomCheckScreen")
                }

                DrawerItem(
                    "Địa chỉ giao hàng",
                    Icons.Default.LocationOn
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("AddressScreen")
                }

                DrawerItem(
                    "Hỗ trợ & FAQ",
                    Icons.Default.Help
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("SupportScreen")
                }
                Spacer(modifier = Modifier.weight(1f))

                DrawerItem(
                    "Đăng xuất",
                    Icons.Default.Logout
                ) {
                    scope.launch { drawerState.close() }
                    authViewModel.logout()

                    Toast.makeText(
                        context,
                        "Đã đăng xuất",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (selectedBottomItem == BottomNavItem.Home) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF7C3AED), Color(0xFFEC4899)),
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
                                    "Hello, Son",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    "Welcome back 👋",
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        navController.navigate("NotificationsScreen")
                                    }
                                ) {
                                    Icon(
                                        Icons.Outlined.Notifications,
                                        null,
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (!isLoggedIn) {
                                            Toast.makeText(
                                                context,
                                                "Vui lòng đăng nhập",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            navController.navigate("CartContent")
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Outlined.ShoppingCart,
                                        null,
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = "Menu",
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
                                    if (selected)
                                        item.filledIcon
                                    else
                                        item.outlinedIcon,
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
                                        } else {
                                            selectedBottomItem = item
                                        }
                                    }

                                    else -> selectedBottomItem = item
                                }
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFEC4899),
                                selectedTextColor = Color(0xFFEC4899),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor =
                                Color(0xFFEC4899).copy(alpha = 0.1f)
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
            ) {
                when (selectedBottomItem) {
                    BottomNavItem.Home ->
                        HomeContent(navController)
                    BottomNavItem.Cart ->
                        OrderHistoryScreen(navController)
                }
            }
        }
    }
}