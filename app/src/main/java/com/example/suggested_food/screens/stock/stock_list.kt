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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.suggested_food.models.ProductModel

@Composable
fun StockListSection(
    products: List<ProductModel>
) {
    val today = "2026-05-08"
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(products) { item ->

            val stockColor = when {
                item.stock == 0 -> Color(0xFFFF5A5F)
                item.stock <= 10 -> Color(0xFFFFB020)
                else -> Color(0xFF22C55E)
            }

            val stockText = when {
                item.stock == 0 -> "Hết hàng"
                item.stock <= 10 -> "Sắp hết"
                else -> "Còn hàng"
            }

            val stockBg = stockColor.copy(alpha = 0.12f)

            val expiryColor = when {
                item.expiryDate.isBlank() -> Color.Gray
                item.expiryDate < today -> Color(0xFFFF5A5F)
                item.expiryDate <= "2026-06-01" -> Color(0xFFFFB020)
                else -> Color(0xFF22C55E)
            }

            val expiryBg = expiryColor.copy(alpha = 0.12f)

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
                                color = stockBg,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stockText,
                            color = stockColor,
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
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
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

                            if (item.expiryDate.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = expiryBg,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "HSD: ${item.expiryDate}",
                                        color = expiryColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

