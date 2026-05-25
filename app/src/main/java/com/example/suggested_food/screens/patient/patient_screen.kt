package com.example.suggested_food.screens.patient

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.models.Patient
import com.example.suggested_food.viewmodels.PatientViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientScreen(
    navController: NavController
) {
    val viewModel: PatientViewModel = viewModel()
    val patients by viewModel.patients.collectAsState()
    var keyword by remember { mutableStateOf("") }
    val filteredPatients = patients.filter {
        it.name.contains(keyword, true) ||
                it.phone.contains(keyword)
    }
    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        delay(1500)
        isLoading = false
    }

    Scaffold(
        containerColor = Color.White,
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
                TopAppBar(
                    title = {
                        Text(
                            "Hồ sơ bệnh nhân",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
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

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(15.dp))

            TextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text("Tìm theo tên hoặc số điện thoại...")
                },

                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },

                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(10.dp))
            if (isLoading) {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(6) {
                        PatientShimmerItem()
                    }
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    itemsIndexed(filteredPatients) { index, patient ->
                        var visible by remember(
                            index,
                            filteredPatients.size
                        ) {
                            mutableStateOf(false)
                        }

                        LaunchedEffect(
                            index,
                            filteredPatients.size
                        ) {

                            visible = false
                            delay(index * 55L)
                            visible = true
                        }

                        val alpha by animateFloatAsState(
                            targetValue = if (visible) 1f else 0f,

                            animationSpec = tween(
                                durationMillis = 650,
                                easing = FastOutSlowInEasing
                            ),

                            label = ""
                        )

                        val translationY by animateFloatAsState(
                            targetValue = if (visible) 0f else 28f,

                            animationSpec = tween(
                                durationMillis = 650,
                                easing = FastOutSlowInEasing
                            ),

                            label = ""
                        )

                        Box(
                            modifier = Modifier.graphicsLayer {
                                this.alpha = alpha
                                this.translationY = translationY
                            }
                        ) {

                            PatientCard(
                                patient = patient,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
