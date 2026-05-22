package com.example.suggested_food.screens.reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.models.ProductModel
import com.example.suggested_food.models.ReminderEntity
import com.example.suggested_food.viewmodels.ReminderViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var actionType by remember { mutableStateOf("CUSTOM") }

    var triggerTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val products by productViewModel.products.collectAsState()
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var repeatInterval by remember { mutableIntStateOf(0) }

    val repeatOptions = listOf(
        0 to "Không lặp",
        5 to "5 phút/lần",
        10 to "10 phút/lần",
        15 to "15 phút/lần",
        30 to "30 phút/lần",
        60 to "1 giờ/lần"
    )

    val bg = Color(0xFFF5F5F5)

    val formattedTime = remember(triggerTime) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(triggerTime))
    }

    Scaffold(
        containerColor = bg,

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
                            "Tạo lịch nhắc",
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
        },

        bottomBar = {
            Button(
                onClick = {
                    if (
                        title.isNotBlank() &&
                        selectedProduct != null
                    ) {
                        viewModel.addReminder(
                            context,
                            ReminderEntity(
                                title = title,
                                description = description,
                                actionType = actionType,
                                medicineId =
                                selectedProduct!!
                                    .id
                                    .toLongOrNull(),
                                medicineName =
                                selectedProduct!!.name,
                                triggerTime = triggerTime,
                                repeatInterval =
                                repeatInterval,
                                isDone = false,
                                isEnabled = true,
                                createdAt =
                                System.currentTimeMillis()
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Lưu nhắc nhở")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionTitle("Thông tin nhắc nhở")

            FormField("Tiêu đề", title, { title = it })
            FormField("Mô tả", description, { description = it })
            SectionTitle("Loại nhắc nhở")

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                FilterChip(
                    selected = actionType == "STOCK_IN",
                    onClick = { actionType = "STOCK_IN" },
                    label = { Text("Nhập thuốc") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        selectedContainerColor = Color(0xFF03A9F4),
                        labelColor = Color.Black,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )

                FilterChip(
                    selected = actionType == "STOCK_OUT",
                    onClick = { actionType = "STOCK_OUT" },
                    label = { Text("Xuất thuốc") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        selectedContainerColor = Color(0xFF03A9F4),
                        labelColor = Color.Black,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )
            }

            SectionTitle("Thuốc")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                TextField(
                    value = selectedProduct?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chọn thuốc") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF03A9F4)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                selectedProduct = product
                                expanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->

                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        val cal = Calendar.getInstance()
                                        cal.set(year, month, day, hour, minute, 0)
                                        triggerTime = cal.timeInMillis
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                ).show()

                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Thời gian",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Chọn ngày",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF03A9F4)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF03A9F4)
                    )
                }
            }

            Text(
                "⏰ $formattedTime",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun FormField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Spacer(Modifier.height(6.dp))

        TextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFF03A9F4)
            )
        )
    }
}