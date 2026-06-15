package com.example.suggested_food.screens.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.suggested_food.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val user = viewModel.user.value
    val loading = viewModel.loading.value
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var showSuccessDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(uid) {
        uid?.let { viewModel.loadCurrentUser(it) }
    }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            email = it.email
            address = it.address
        }
    }

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(80)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,

        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

    val translationY by animateFloatAsState(
        targetValue = if (visible) 0f else 35f,

        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2563EB), Color(0xFF38BDF8))
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Thông tin cá nhân",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
                .offset(y = translationY.dp)
                .background(Color(0xFFF5F5F5))
                .padding(padding)
        ) {

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                return@Box
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    ProfileInput("Email", email, false) { email = it }
                    ProfileInput("Tên", name, true) { name = it }
                    ProfileInput("Địa chỉ", address, true) { address = it }

                    val date = user?.createdAt?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                    } ?: ""

                    ProfileInput("Ngày tạo tài khoản", date, false) {}
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(deepBlue, blue)
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            user?.let {
                                viewModel.updateUser(
                                    it.copy(
                                        name = name,
                                        address = address
                                    )
                                )

                                showSuccessDialog = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lưu thông tin",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Text(
                    "Cập nhật thành công",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Thông tin của bạn đã được lưu.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = deepBlue
                    )
                ) {
                    Text("Đóng")
                }
            }
        )
    }
}
