package com.example.suggested_food.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodels.AuthViewModel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Trang chủ")
    object Categories : BottomNavItem("categories", Icons.Default.List, "Danh mục")
    object Cart : BottomNavItem("cart", Icons.Default.ShoppingCart, "Giỏ hàng")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Tài khoản")
}

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
        BottomNavItem.Categories,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    var selectedBottomItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.5f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "Menu",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    DrawerItem("Profile") { navController.navigate("profile") }
                    DrawerItem("Thông báo") { /* TODO */ }
                    DrawerItem("Cài đặt") { /* TODO */ }
                    DrawerItem("Đăng xuất") {
                        authViewModel.logout()
                        navController.navigate("login") { popUpTo("home") { inclusive = true } }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(selectedBottomItem.label) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selectedBottomItem.route == item.route,
                            onClick = { selectedBottomItem = item }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedBottomItem) {
                    BottomNavItem.Home -> HomeContent()
                    BottomNavItem.Categories -> CategoriesContent()
                    BottomNavItem.Cart -> CartContent()
                    BottomNavItem.Profile -> ProfileContent()
                }
            }
        }
    }
}

@Composable
fun DrawerItem(name: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Text(name, style = MaterialTheme.typography.bodyLarge)
    }
}

