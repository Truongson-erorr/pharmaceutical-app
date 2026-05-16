package com.example.suggested_food.screens.drug_look_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.suggested_food.viewmodels.DrugLookupViewModel
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugLookupScreen(
    navController: NavController,
    viewModel: DrugLookupViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
                TopAppBar(
                    title = {
                        Text(
                            "Tra cứu thuốc offline",
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
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                TextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    placeholder = {
                        Text("Nhập tên thuốc...", color = Color(0xFF6B7280))
                    },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            if (query.isNotBlank())
                                viewModel.searchDrug(query)
                        }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF6B7280)
                            )
                        }
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {

                    repeat(1) {
                        ShimmerDrugLookup()
                        Spacer(Modifier.height(16.dp))
                    }
                }

                else if (!result.isNullOrBlank()) {

                    val lines = result!!.lines()

                    var imageUrl: String? = null
                    if (lines.isNotEmpty()) {
                        imageUrl = lines[0]
                    }

                    imageUrl?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    val textLines =
                        if (lines.size > 1) lines.drop(1) else emptyList()

                    val sections = textLines
                        .joinToString("\n")
                        .split("## ")
                        .filter { it.isNotBlank() }

                    sections.forEach { section ->

                        val sectionLines = section.trim().lines()

                        if (sectionLines.isNotEmpty()) {

                            Text(
                                text = sectionLines[0],
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(
                                    top = 12.dp,
                                    bottom = 4.dp
                                )
                            )

                            Text(
                                text = sectionLines.drop(1).joinToString("\n"),
                                fontSize = 13.sp,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                }

                else {
                    Text(
                        text = "Nhập tên thuốc và nhấn tìm kiếm để xem thông tin đầy đủ.",
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerDrugLookup() {

    val shimmerColor = Color(0xFFC7CBD1)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(shimmerColor)
            )

            Box(
                Modifier
                    .fillMaxWidth(0.5f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerColor)
            )

            repeat(3) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerColor)
                )
            }

            Spacer(Modifier.height(6.dp))

            Box(
                Modifier
                    .fillMaxWidth(0.4f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerColor)
            )

            repeat(5) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerColor)
                )
            }

            Spacer(Modifier.height(6.dp))

            Box(
                Modifier
                    .fillMaxWidth(0.3f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerColor)
            )

            repeat(4) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerColor)
                )
            }
        }
    }
}