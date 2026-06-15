package com.example.suggested_food.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ProductShimmerItem() {
    val shimmerColor = Color(0xFFC7CBD1)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .shimmer()
                    .background(shimmerColor)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(18.dp)
                    .shimmer()
                    .background(
                        shimmerColor,
                        RoundedCornerShape(6.dp)
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(24.dp)
                    .shimmer()
                    .background(
                        shimmerColor,
                        RoundedCornerShape(50)
                    )
            )
        }
    }
}