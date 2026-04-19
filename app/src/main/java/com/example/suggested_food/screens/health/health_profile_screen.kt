package com.example.suggested_food.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.suggested_food.models.HealthProfile
import com.example.suggested_food.viewmodels.AuthViewModel
import com.example.suggested_food.viewmodels.HealthProfileViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: HealthProfileViewModel = viewModel()
) {
    val user = authViewModel.getCurrentUser()
    val userId = user?.uid ?: return

    val profile by viewModel.profile.collectAsState()

    var birthDate by remember(profile) { mutableStateOf(profile.birthDate) }
    var gender by remember(profile) { mutableStateOf(profile.gender) }
    var bloodType by remember(profile) { mutableStateOf(profile.bloodType) }

    var medicalHistory by remember(profile) {
        mutableStateOf(profile.medicalHistory)
    }

    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile(userId)
    }

    val diseases = listOf(
        "Tiểu đường","Cao huyết áp","Tim mạch","Hen suyễn","Ung thư",
        "Đột quỵ","Béo phì","Mỡ máu cao","Gan nhiễm mỡ","Viêm gan B",
        "Viêm gan C","Suy thận","Loãng xương","Viêm khớp","Dạ dày",
        "Trào ngược dạ dày","Dị ứng","Rối loạn tuyến giáp","Thiếu máu",
        "Trầm cảm","Rối loạn lo âu","COPD","COVID-19 hậu di chứng",
        "Parkinson","Alzheimer"
    )

    Box {

        Scaffold(
            containerColor = Color(0xFFF5F5F5),

            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            "Hồ sơ sức khỏe",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton({ navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBackIos, null, tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            },

            bottomBar = {
                Surface(tonalElevation = 8.dp) {
                    Button(
                        enabled = !isSaving,
                        onClick = {
                            isSaving = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5848CE)
                        )
                    ) {
                        Text("Lưu hồ sơ", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                ModernCard("Thông tin cơ bản") {

                    ModernTextField(
                        birthDate,
                        { birthDate = it },
                        "Ngày sinh",
                        Icons.Default.Cake
                    )
                    Spacer(Modifier.height(12.dp))

                    DropdownField(
                        "Giới tính",
                        listOf("Nam", "Nữ", "Khác"),
                        gender,
                        Icons.Default.Person
                    ) { gender = it }

                    Spacer(Modifier.height(12.dp))

                    DropdownField(
                        "Nhóm máu",
                        listOf("A", "B", "AB", "O"),
                        bloodType,
                        Icons.Default.Bloodtype
                    ) { bloodType = it }
                }
                Spacer(Modifier.height(16.dp))

                ModernCard("Tiền sử bệnh") {
                    diseases.forEach { disease ->

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .toggleable(
                                    value = medicalHistory.contains(disease),
                                    onValueChange = { checked ->
                                        medicalHistory =
                                            if (checked)
                                                medicalHistory + disease
                                            else
                                                medicalHistory - disease
                                    }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = medicalHistory.contains(disease),
                                onCheckedChange = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(disease)
                        }
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }

        if (isSaving) {
            LaunchedEffect(Unit) {
                delay(2000)

                viewModel.saveProfile(
                    HealthProfile(
                        userId,
                        birthDate,
                        gender,
                        bloodType,
                        medicalHistory
                    )
                )

                isSaving = false
                navController.popBackStack()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator(
                            color = Color(0xFF5848CE)
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            "Đang lưu hồ sơ...",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
