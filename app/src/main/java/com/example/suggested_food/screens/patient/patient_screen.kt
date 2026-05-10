package com.example.suggested_food.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.models.Patient
import com.example.suggested_food.viewmodels.PatientViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

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

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Hồ sơ khách hàng",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPatients) {
                    PatientCard(it)
                }
            }
        }
    }
}

@Composable
fun PatientCard(patient: Patient) {
    val currency = NumberFormat.getInstance(Locale("vi", "VN"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { },
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
            }
            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))

                Text(
                    text = patient.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {
                        Text(
                            "Đơn hàng",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "${patient.totalOrders}",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            "Tổng chi",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${currency.format(patient.totalSpent)} đ",
                                color = Color(0xFF22C55E),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Column {
                        Text(
                            "Lần cuối mua",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text =
                                if (patient.lastVisit != 0L)
                                    dateFormat.format(Date(patient.lastVisit))
                                else "--",
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}