package com.example.suggested_food.screens.health

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
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

@RequiresApi(Build.VERSION_CODES.O)
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
    var medicalHistory by remember(profile) { mutableStateOf(profile.medicalHistory) }

    var height by remember(profile) { mutableStateOf(profile.height.toString()) }
    var weight by remember(profile) { mutableStateOf(profile.weight.toString()) }

    var isSaving by remember { mutableStateOf(false) }
    var expandedDisease by remember { mutableStateOf(false) }

    val diseases = listOf(
        "Cao huyết áp",
        "Hạ huyết áp",
        "Rối loạn mỡ máu",
        "Béo phì",
        "Thiếu cân",
        "Thiếu máu",
        "Bệnh tim mạch vành",
        "Suy tim",
        "Rối loạn nhịp tim",
        "Đột quỵ",
        "Xơ vữa động mạch",
        "Hen suyễn",
        "Viêm phổi",
        "Viêm phế quản",
        "Cảm cúm mãn tính",
        "Viêm dạ dày",
        "Loét dạ dày",
        "Trào ngược dạ dày",
        "Viêm đại tràng",
        "Hội chứng ruột kích thích",
        "Gan nhiễm mỡ",
        "Viêm gan B",
        "Viêm gan C",
        "Suy thận",
        "Sỏi thận",
        "Viêm đường tiết niệu",
        "Viêm khớp",
        "Thoái hóa khớp",
        "Loãng xương",
        "Đau lưng mãn tính",
        "Động kinh",
        "Đau nửa đầu",
        "Rối loạn lo âu",
        "Trầm cảm",
        "Rối loạn tuyến giáp",
        "Dị ứng thời tiết",
        "Dị ứng thực phẩm",
        "Dị ứng thuốc",
        "Viêm da dị ứng",
        "COVID-19 hậu di chứng",
        "Sốt xuất huyết",
        "Viêm gan virus",
        "Nhiễm trùng mãn tính",
        "Suy giảm miễn dịch",
        "Mệt mỏi mãn tính"
    )

    val bmi = remember(height, weight) {
        val h = height.toFloatOrNull()
        val w = weight.toFloatOrNull()

        if (h != null && w != null && h > 0)
            w / ((h / 100) * (h / 100))
        else null
    }

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),

        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Hồ sơ sức khỏe",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton({ navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIos, null, tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },

        bottomBar = {
            Surface(tonalElevation = 0.dp) {
                Button(
                    enabled = !isSaving,
                    onClick = { isSaving = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Lưu hồ sơ",
                        fontWeight = FontWeight.Bold
                    )
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

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(
                    Modifier.padding(16.dp)
                ) {

                    Text("Thông tin cơ bản", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    DatePickerField(
                        value = birthDate,
                        onDateSelected = { birthDate = it },
                        label = "Ngày sinh"
                    )
                    Spacer(Modifier.height(12.dp))

                    DropdownField("Giới tính", listOf("Nam","Nữ","Khác"), gender) {
                        gender = it
                    }

                    Spacer(Modifier.height(12.dp))

                    DropdownField("Nhóm máu", listOf("A","B","AB","O"), bloodType) {
                        bloodType = it
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            HealthBmiCard(
                height = height,
                weight = weight,
                bmi = bmi,
                onHeightChange = { height = it },
                onWeightChange = { weight = it }
            )
            Spacer(Modifier.height(16.dp))

            HealthMedicalHistoryCard(
                medicalHistory = medicalHistory,
                expanded = expandedDisease,
                onExpandedChange = { expandedDisease = it },
                diseases = diseases,
                onToggleDisease = { disease ->
                    medicalHistory =
                        if (medicalHistory.contains(disease))
                            medicalHistory - disease
                        else
                            medicalHistory + disease
                }
            )
            Spacer(Modifier.height(40.dp))
        }
    }

    if (isSaving) {
        LaunchedEffect(Unit) {
            delay(1500)

            viewModel.saveProfile(
                HealthProfile(
                    userId,
                    birthDate,
                    gender,
                    bloodType,
                    medicalHistory,
                    height = height.toFloatOrNull() ?: 0f,
                    weight = weight.toFloatOrNull() ?: 0f,
                    bmi = bmi ?: 0f
                )
            )
            isSaving = false
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Black)
        }
    }
}