package com.example.suggested_food.screens.health

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: String,
    onDateSelected: (String) -> Unit,
    label: String
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
    ) {

        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },

            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),

            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color(0xFFF5F5F5),
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Gray
            )
        )
    }

    if (showDatePicker) {

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },

            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            ),

            confirmButton = {
                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            val formatted =
                                "%02d/%02d/%04d".format(
                                    date.dayOfMonth,
                                    date.monthValue,
                                    date.year
                                )

                            onDateSelected(formatted)
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Chọn", color = Color(0xFF2563EB))
                }
            },

            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Huỷ", color = Color.Gray)
                }
            }

        ) {

            DatePicker(
                state = datePickerState,

                colors = DatePickerDefaults.colors(

                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    navigationContentColor = Color(0xFF2563EB),

                    weekdayContentColor = Color.Gray,
                    dayContentColor = Color.Black,
                    selectedDayContainerColor = Color(0xFF2563EB),
                    selectedDayContentColor = Color.White,

                    todayDateBorderColor = Color(0xFF2563EB),
                    selectedYearContainerColor = Color(0xFF2563EB),
                    selectedYearContentColor = Color.White
                )
            )
        }
    }
}