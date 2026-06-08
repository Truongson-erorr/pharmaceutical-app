package com.example.suggested_food.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsCard(
    items: List<SettingItem>,
    onItemClick: (SettingItem) -> Unit,
    onLogoutClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        items.forEach { item ->

            Box(
                modifier = Modifier.clickable {

                    if (item.title == "Đăng xuất") {
                        onLogoutClick?.invoke()
                    } else {
                        onItemClick(item)
                    }

                }
            ) {
                SettingRow(item = item)
            }
        }
    }
}

@Composable
private fun SettingRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            if (item.subtitle != null) {
                Text(
                    text = item.subtitle,
                    fontSize = 15.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        if (item.value != null) {
            Text(
                text = item.value,
                fontSize = 15.sp,
                color = Color(0xFF374151)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(20.dp)
        )
    }
}
