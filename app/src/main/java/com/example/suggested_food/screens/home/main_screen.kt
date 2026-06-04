package com.example.suggested_food.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.screens.settings.SettingsScreen
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userName by authViewModel.userName.collectAsState()
    val user = authViewModel.getCurrentUser()
    val displayName = userName ?: "Chuyên gia"

    val notificationViewModel: NotificationViewModel = viewModel()
    val count by notificationViewModel.notifCount.collectAsState()

    LaunchedEffect(Unit) {
        notificationViewModel.loadNotifications()
    }
    var selectedTab by remember { mutableStateOf(0) }

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
                                start = 18.dp,
                                end = 22.dp,
                                top = 40.dp,
                                bottom = 20.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Hello, $displayName",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Dành cho chuyên gia",
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = {
                                    navController.navigate("NotificationScreen")
                                }
                            ) {
                                Icon(
                                    Icons.Default.NotificationsNone,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            if (count > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-2).dp, y = 2.dp)
                                        .size(23.dp)
                                        .background(
                                            Color(0xFFFF3B30),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (count > 99) "99+" else count.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
        },

        bottomBar = {
            AnimatedBottomBar(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                }
            )
        },

        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when (selectedTab) {
                0 -> { HomeContent(navController) }
                1 -> { SettingsScreen(authViewModel = authViewModel) }
            }
        }
    }
}

@Composable
fun AnimatedBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {

            val tabWidth = maxWidth / 2
            val indicatorOffset by animateDpAsState(
                targetValue =
                if (selectedTab == 0)
                    0.dp
                else
                    tabWidth,
                animationSpec = tween(300),
                label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(tabWidth)
                        .height(55.dp)
                        .padding(horizontal = 4.dp)
                        .graphicsLayer {
                            shadowElevation = 8.dp.toPx()
                            shape = RoundedCornerShape(26.dp)
                            clip = false
                        }
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFF38BDF8)
                                )
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                )

                Row(
                    modifier = Modifier.fillMaxSize()
                ) {

                    BottomBarItem(
                        modifier = Modifier.weight(1f),
                        selected = selectedTab == 0,
                        icon = Icons.Outlined.Home,
                        title = "Trang chủ"
                    ) {
                        onTabSelected(0)
                    }

                    BottomBarItem(
                        modifier = Modifier.weight(1f),
                        selected = selectedTab == 1,
                        icon = Icons.Outlined.Settings,
                        title = "Cài đặt"
                    ) {
                        onTabSelected(1)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val contentColor =
        if (selected)
            Color.White
        else
            Color.Gray

    Row(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(26.dp))
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}