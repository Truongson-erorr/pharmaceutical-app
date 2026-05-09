package com.example.suggested_food.screens.reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.models.ProductModel
import com.example.suggested_food.models.ReminderEntity
import com.example.suggested_food.viewmodel.ReminderViewModel
import com.example.suggested_food.viewmodels.ProductViewModel
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel,
    productViewModel: ProductViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var actionType by remember { mutableStateOf("CUSTOM") }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var triggerTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val products by productViewModel.products.collectAsState()
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var repeatInterval by remember { mutableStateOf(0) }

    val repeatOptions = listOf(
        0 to "Không lặp",
        5 to "Mỗi 5 phút",
        10 to "Mỗi 10 phút",
        15 to "Mỗi 15 phút",
        30 to "Mỗi 30 phút",
        60 to "Mỗi 1 giờ"
    )

    val formattedTime = remember(triggerTime) {
        java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
            .format(Date(triggerTime))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tạo nhắc nhở",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBackIos,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        bottomBar = {

            Button(
                onClick = {
                    if (title.isNotBlank() && selectedProduct != null) {

                        viewModel.addReminder(
                            ReminderEntity(
                                title = title,
                                description = description,
                                actionType = actionType,

                                medicineId = selectedProduct!!.id.toLongOrNull(),
                                medicineName = selectedProduct!!.name,

                                triggerTime = triggerTime,
                                repeatInterval = repeatInterval
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tiêu đề") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Mô tả") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Text("Loại:")
            Row {
                FilterChip(
                    selected = actionType == "STOCK_IN",
                    onClick = { actionType = "STOCK_IN" },
                    label = { Text("Nhập thuốc") }
                )

                Spacer(Modifier.width(8.dp))

                FilterChip(
                    selected = actionType == "STOCK_OUT",
                    onClick = { actionType = "STOCK_OUT" },
                    label = { Text("Xuất thuốc") }
                )
            }
            Spacer(Modifier.height(16.dp))

            Text("💊 Chọn thuốc:")
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedProduct?.name ?: "Chọn thuốc")
                }

                DropdownMenu(
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
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {

                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->

                            TimePickerDialog(
                                context,
                                { _, hour, minute ->

                                    val cal = Calendar.getInstance()
                                    cal.set(year, month, dayOfMonth, hour, minute, 0)

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

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Chọn ngày & giờ")
            }
            Spacer(Modifier.height(8.dp))

            Text("⏰ $formattedTime")
            Spacer(Modifier.height(16.dp))

            Text("Lặp lại:")

            Column {
                repeatOptions.forEach { (value, label) ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(
                            selected = repeatInterval == value,
                            onClick = { repeatInterval = value }
                        )
                        Text(label)
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}