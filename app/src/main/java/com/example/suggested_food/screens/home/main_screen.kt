package com.example.suggested_food.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.NavigationDrawerItemDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.NotificationViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()
    val displayName = userName ?: "Chuyên gia"
    val email = user?.email ?: "Chưa có email"
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route
    var showLogoutDialog by remember { mutableStateOf(false) }

    val notificationViewModel: NotificationViewModel = viewModel()
    val count by notificationViewModel.notifCount.collectAsState()
    LaunchedEffect(Unit) {
        notificationViewModel.loadNotifications()
    }

    @Composable
    fun DrawerItem(
        title: String,
        icon: ImageVector,
        route: String
    ) {
        val selected = currentRoute == route
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            NavigationDrawerItem(
                selected = selected,
                onClick = {
                    scope.launch { drawerState.close() }

                    navController.navigate(route) {
                        popUpTo("MainScreen")
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        icon,
                        contentDescription = title,
                        tint = if (selected) Color(0xFF38BDF8) else Color.Gray
                    )
                },
                label = {
                    Text(
                        title,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) Color(0xFF38BDF8) else Color.Black
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.98f),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFF38BDF8).copy(alpha = 0.15f),
                    unselectedContainerColor = Color.Transparent
                )
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Text(
                    text = "Xác nhận đăng xuất",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = "Bạn có chắc muốn đăng xuất khỏi hệ thống không?",
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false

                        scope.launch { drawerState.close() }

                        authViewModel.logout()

                        Toast.makeText(
                            context,
                            "Đã đăng xuất",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text(
                        "Đăng xuất",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(
                        "Hủy",
                        color = Color.Gray
                    )
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.7f),
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFF38BDF8)
                                )
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
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))

                    Text(
                        displayName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        email,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    DrawerItem("Trang chủ", Icons.Default.Home, "MainScreen")
                    DrawerItem("Hồ sơ khách hàng", Icons.Default.PeopleAlt, "PatientScreen")
                    DrawerItem("Lịch nhắc nhở", Icons.Default.NotificationsActive, "ReminderScreen")
                    DrawerItem("Mã khuyến mãi", Icons.Default.LocalOffer, "PromoCodeScreen")
                    DrawerItem("Quản lý kho thuốc", Icons.Default.Cable, "InventoryScreen")
                    DrawerItem("Quản lý tồn kho", Icons.Default.Storefront, "StockScreen")
                    DrawerItem("Hóa đơn", Icons.Default.ReceiptLong, "InvoiceDashboardScreen")
                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clickable {
                                showLogoutDialog = true
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        Spacer(Modifier.width(12.dp))

                        Text(
                            "Đăng xuất",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFF38BDF8)
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
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                "Hello, $displayName",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                "Dành cho chuyên gia",
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box {
                                IconButton(
                                    onClick = {
                                        navController.navigate("NotificationScreen")
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.NotificationsNone,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }

                                if (count > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = (-2).dp, y = 2.dp)
                                            .size(23.dp)
                                            .background(
                                                color = Color(0xFFFF3B30),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (count > 99) "99+" else count.toString(),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            },
            containerColor = Color(0xFFF5F5F5)
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                HomeContent(navController)
            }
        }
    }
}