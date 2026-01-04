package com.example.suggested_food.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Outlined.Home, "Trang chủ")
    object Utilities : BottomNavItem("utilities", Icons.Outlined.Lightbulb, "Tiện ích")
    object Cart : BottomNavItem("cart", Icons.Outlined.LocalHospital, "Đơn thuốc")
    object Profile : BottomNavItem("profile", Icons.Outlined.Person, "Tôi")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f),
                drawerContainerColor = Color(0xFF8B0000)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp, top = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = Color.White
                    )

                    Divider(color = Color.White.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(8.dp))

                    DrawerItemWithIcon("Thông báo", Icons.Outlined.Notifications) { }
                    DrawerItemWithIcon("Cài đặt", Icons.Outlined.Settings) { }
                    DrawerItemWithIcon("Hỗ trợ", Icons.Outlined.HelpOutline) { }
                    DrawerItemWithIcon("Về ứng dụng", Icons.Outlined.Info) { }
                    if (isLoggedIn) {
                        DrawerItemWithIcon("Đăng xuất", Icons.Outlined.Logout) {
                            authViewModel.logout()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    } else {
                        DrawerItemWithIcon("Đăng nhập", Icons.Outlined.Login) {
                            navController.navigate("login")
                        }
                    }

                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Nhà thuốc SK",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xFF8B0000)
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(text = item.label)
                            },
                            selected = selectedBottomItem.route == item.route,
                            onClick = {
                                when (item) {
                                    BottomNavItem.Profile,
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
                                selectedIconColor = Color(0xFF8B0000),
                                selectedTextColor = Color(0xFF8B0000),
                                unselectedIconColor = Color(0xFF9CA3AF),
                                unselectedTextColor = Color(0xFF9CA3AF),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                when (selectedBottomItem) {
                    BottomNavItem.Home -> HomeContent(navController, authViewModel = authViewModel)
                    BottomNavItem.Utilities -> UtilitiesContent(navController)
                    BottomNavItem.Cart -> MedicineScreen(navController)
                    BottomNavItem.Profile -> ProfileContent(navController, authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun DrawerItemWithIcon(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}
