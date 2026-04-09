package com.example.suggested_food.screens.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.suggested_food.models.CartItemModel
import com.example.suggested_food.screens.home.formatVND

@Composable
fun CartItemRow(
    item: CartItemModel,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xóa sản phẩm") },
            text = { Text("Bạn có chắc muốn xóa sản phẩm này không?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onRemove()
                }) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )

            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold)
                Text(
                    text = formatVND(item.price),
                    color = Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrease) {
                        Text("−", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                    }
                    Text("${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = onIncrease) {
                        Text("+", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                    }
                }
            }

            Surface(
                onClick = { showDialog = true },
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Xóa",
                    color = Color(0xFF5848CE),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

