package com.example.suggested_food.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerSlider() {

    val banners = remember {
        listOf(
            "https://bcp.cdnchinhphu.vn/334894974524682240/2025/10/24/long-chau-3-1761313032132863736386.jpg",
            "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2025/5/12/tpcn-1747054390355236451777.jpg",
            "https://develop-cdn.pharmacity.io/digital/original/plain/blog/c06a50e61fa3fa330a3c629eabdce74e1733536834.jpg"
        )
    }

    val pagerState = rememberPagerState(
        pageCount = { banners.size }
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) { page ->
        AsyncImage(
            model = banners[page],
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
