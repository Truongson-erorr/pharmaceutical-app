package com.example.suggested_food.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HealthMedicalHistoryCard(
    medicalHistory: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    diseases: List<String>,
    onToggleDisease: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            Modifier.padding(16.dp)
        ) {

            Text(
                text = "Tiền sử bệnh",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = onExpandedChange
            ) {

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Chọn bệnh") },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier.background(Color.White)
                ) {

                    diseases.forEach { disease ->

                        val selected = medicalHistory.contains(disease)

                        DropdownMenuItem(
                            text = {
                                Row {
                                    Checkbox(
                                        checked = selected,
                                        onCheckedChange = null
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(disease)
                                }
                            },
                            onClick = { onToggleDisease(disease) }
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (medicalHistory.isEmpty()) {
                    Text(
                        text = "Chưa chọn bệnh",
                        color = Color.Gray
                    )
                } else {
                    medicalHistory.forEach { disease ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFE0F2FE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = disease,
                                color = Color(0xFF0284C7),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}