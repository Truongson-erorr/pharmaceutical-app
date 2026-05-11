package com.example.suggested_food.screens.promotion

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.models.PromoCode
import com.example.suggested_food.viewmodels.PromoCodeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPromoCodeScreen(
    navController: NavController,
    viewModel: PromoCodeViewModel
) {
    val context = LocalContext.current
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var discountType by remember { mutableStateOf("PERCENT") }
    var discountValue by remember { mutableStateOf("0") }
    var maxDiscount by remember { mutableStateOf("0") }

    var minOrderValue by remember { mutableStateOf("0") }
    var usageLimit by remember { mutableStateOf("0") }
    var perUserLimit by remember { mutableStateOf("1") }

    var applyScope by remember { mutableStateOf("ALL") }

    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }

    var internalNote by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }

    fun formatDate(time: Long?): String {
        if (time == null) return "Chưa chọn"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(time))
    }
    Scaffold(
        containerColor = Color(0xFFF5F5F5),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tạo mã khuyến mãi",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIos, null)
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
                    val promo = PromoCode(
                        code = code,
                        name = name,
                        description = description.ifBlank { null },
                        discountType = discountType,
                        discountValue = discountValue.toDoubleOrNull() ?: 0.0,
                        maxDiscountAmount = maxDiscount.toDoubleOrNull(),
                        minOrderValue = minOrderValue.toDoubleOrNull() ?: 0.0,
                        usageLimit = usageLimit.toIntOrNull() ?: 0,
                        perUserLimit = perUserLimit.toIntOrNull() ?: 1,
                        applyScope = applyScope,
                        startDate = startDate ?: 0L,
                        endDate = endDate ?: 0L,
                        isActive = isActive,
                        internalNote = internalNote.ifBlank { null }
                    )

                    viewModel.addPromoCode(promo)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text("Lưu mã", color = Color.White)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Field("Mã code (VD: SALE10)", code) { code = it }
            Field("Tên mã (Tên chương trình)", name) { name = it }
            Field("Mô tả (Thông tin thêm)", description) { description = it }

            Text("Loại giảm (%) hoặc (Tiền mặt)", fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = discountType == "PERCENT",
                    onClick = { discountType = "PERCENT" },
                    label = { Text("%") }
                )
                FilterChip(
                    selected = discountType == "FIXED",
                    onClick = { discountType = "FIXED" },
                    label = { Text("Tiền") }
                )
            }

            Field("Giá trị giảm (Value)", discountValue) { discountValue = it }
            Field("Giảm tối đa (Max discount)", maxDiscount) { maxDiscount = it }
            Field("Đơn tối thiểu (Min order)", minOrderValue) { minOrderValue = it }
            Field("Giới hạn dùng (Usage limit)", usageLimit) { usageLimit = it }
            Field("Giới hạn/user (Per user)", perUserLimit) { perUserLimit = it }
            Text("Phạm vi áp dụng (ALL / PRODUCT)", fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = applyScope == "ALL",
                    onClick = { applyScope = "ALL" },
                    label = { Text("Tất cả") }
                )
                FilterChip(
                    selected = applyScope == "PRODUCT",
                    onClick = { applyScope = "PRODUCT" },
                    label = { Text("Sản phẩm") }
                )
            }

            DatePickerField(
                label = "Ngày bắt đầu (Start date)",
                value = formatDate(startDate),
                onPick = {
                    val calendar = Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    val cal = Calendar.getInstance()
                                    cal.set(year, month, day, hour, minute, 0)
                                    startDate = cal.timeInMillis
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
            )

            DatePickerField(
                label = "Ngày kết thúc (End date)",
                value = formatDate(endDate),
                onPick = {
                    val calendar = Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    val cal = Calendar.getInstance()
                                    cal.set(year, month, day, hour, minute, 0)
                                    endDate = cal.timeInMillis
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
            )

            Field("Ghi chú admin (Internal note)", internalNote) { internalNote = it }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isActive, onCheckedChange = { isActive = it })
                Text("Kích hoạt ngay")
            }
        }
    }
}

@Composable
fun Field(label: String, value: String, onChange: (String) -> Unit) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun DatePickerField(
    label: String,
    value: String,
    onPick: () -> Unit
) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPick() }
                .background(Color.White, RoundedCornerShape(14.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = value, color = Color.Black)

            Text(
                text = "Chọn ngày",
                color = Color(0xFF03A9F4),
                fontWeight = FontWeight.Bold
            )
        }
    }
}