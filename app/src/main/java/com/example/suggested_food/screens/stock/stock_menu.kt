package com.example.suggested_food.screens.stock

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.dp

@Composable
fun StockMenuDropdown(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onImport: () -> Unit,
    onExport: () -> Unit,
    onHistory: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(Color.White)
    ) {

        DropdownMenuItem(
            text = { Text("Nhập thuốc", color = Color.Black) },
            onClick = onImport,
            leadingIcon = { Icon(Icons.Default.Add, null, tint = Color.Gray) }
        )

        DropdownMenuItem(
            text = { Text("Xuất thuốc", color = Color.Black) },
            onClick = onExport,
            leadingIcon = { Icon(Icons.Default.Remove, null, tint = Color.Gray) }
        )

        DropdownMenuItem(
            text = { Text("Lịch sử nhập/xuất", color = Color.Black) },
            onClick = onHistory,
            leadingIcon = { Icon(Icons.Default.History, null, tint = Color.Gray) }
        )
    }
}