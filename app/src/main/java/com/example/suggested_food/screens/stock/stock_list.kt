package com.example.suggested_food.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.suggested_food.models.ProductModel

@Composable
fun StockListSection(products: List<ProductModel>) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(products) { item ->

            val statusColor = when {
                item.stock == 0 -> Color(0xFFC62828)
                item.stock <= 10 -> Color(0xFFF9A825)
                else -> Color(0xFF2E7D32)
            }

            val statusText = when {
                item.stock == 0 -> "Hết hàng"
                item.stock <= 10 -> "Sắp hết"
                else -> "Còn hàng"
            }

            val statusBg = statusColor.copy(alpha = 0.12f)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(
                                color = statusBg,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 70.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = item.images.firstOrNull(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(0xFFE0E0E0))
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                item.name,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )

                            Text(
                                "Tồn: ${item.stock}",
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}