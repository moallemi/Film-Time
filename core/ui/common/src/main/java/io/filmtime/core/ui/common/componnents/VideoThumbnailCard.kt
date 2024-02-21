package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import io.filmtime.data.model.VideoThumbnail

@Composable
fun VideoThumbnailCard(
  modifier: Modifier = Modifier,
  videoThumbnail: VideoThumbnail,
  onClick: () -> Unit,
) {
  Card(
    onClick = onClick,
    modifier = modifier,
  ) {
    VideoThumbnailCardContent(videoThumbnail = videoThumbnail)
  }
}

@Composable
private fun VideoThumbnailCardContent(
  videoThumbnail: VideoThumbnail,
) {
  AsyncImage(
    model = videoThumbnail.posterUrl,
    contentDescription = videoThumbnail.title,
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop,
  )
}
