package com.chilly.android.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.chilly.android.data.remote.dto.PlaceDto

@Composable
fun PlaceListItem(
    place: PlaceDto,
    onclick: () -> Unit
) {
    PlaceCard(
        place,
        onCardClick = { onclick() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = place.address,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PlaceCard(
    place: PlaceDto,
    imageSize: Dp = 80.dp,
    onCardClick: (PlaceDto) -> Unit = {},
    content: @Composable (PlaceDto) -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onCardClick(place)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = place.imageUrls.firstOrNull(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Image(
                        painter = painterResource(place.hashCode().chillyPepperRes()),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(imageSize)
                    .border(width = 0.5.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            )
            content(place)
        }
    }
}

@Composable
fun PlaceImagesPager(
    place: PlaceDto
) {
    val initialPage = if (place.imageUrls.size > 1) 1 else 0
    val pagerState = rememberPagerState(initialPage = initialPage) { place.imageUrls.size }
    Column {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            SubcomposeAsyncImage(
                model = place.imageUrls[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .border(width = 0.5.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(12.dp))
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            repeat(pagerState.pageCount) { index ->
                val (color, size) = with(MaterialTheme.colorScheme) {
                    if (index == pagerState.currentPage)
                        onSurface to 8.dp
                    else
                        secondary to 6.dp
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 6.dp)
                        .clip(CircleShape)
                        .size(size)
                        .background(color)
                )
            }
        }
    }
}