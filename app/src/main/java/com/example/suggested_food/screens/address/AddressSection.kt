package com.example.suggested_food.screens.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.suggested_food.models.UserModel

@Composable
fun AddressSection(
    user: UserModel?,
    navController: NavController
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Địa chỉ nhận hàng",
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.navigate("address")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (user?.address.isNullOrBlank()) {
            Text(
                "Chưa có địa chỉ giao hàng",
                color = Color.Red
            )
        } else {
            if (user != null) {
                Text(
                    user.address,
                    color = Color.Gray
                )
            }
        }
    }
}

