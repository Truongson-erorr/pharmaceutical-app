package com.example.suggested_food.screens.product

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProductDetailShimmer() {
    Column {

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        )
        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(28.dp)
            )
            Spacer(Modifier.height(12.dp))

            ShimmerBox(
                modifier = Modifier
                    .width(120.dp)
                    .height(22.dp)
            )
            Spacer(Modifier.height(16.dp))

            Row {

                ShimmerBox(
                    modifier = Modifier
                        .width(60.dp)
                        .height(18.dp)
                )
                Spacer(Modifier.width(12.dp))

                ShimmerBox(
                    modifier = Modifier
                        .width(110.dp)
                        .height(18.dp)
                )
            }
            Spacer(Modifier.height(24.dp))

            repeat(4) {
                ShimmerBox(
                    modifier = Modifier
                        .width(140.dp)
                        .height(20.dp)
                )
                Spacer(Modifier.height(10.dp))

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Spacer(Modifier.height(8.dp))

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(16.dp)
                )
                Spacer(Modifier.height(8.dp))

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(16.dp)
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {

    val transition = rememberInfiniteTransition(
        label = ""
    )

    val translateAnim = transition.animateFloat(
        initialValue = -500f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE5E7EB),
            Color(0xFFF3F4F6),
            Color(0xFFE5E7EB)
        ),
        start = Offset.Zero,
        end = Offset(
            translateAnim.value,
            translateAnim.value
        )
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}