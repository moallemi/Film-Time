package io.filmtime.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import io.filmtime.data.model.VideoThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoThumbnailCard(
  modifier: Modifier = Modifier,
  videoThumbnail: VideoThumbnail,
  onClick: () -> Unit,
) {
  var selected by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(if (selected) 1.1f else 1f, label = "scale-transition")

  Card(
    onClick = onClick,
    modifier = modifier
      .scale(scale)
      .onFocusChanged {
        selected = it.isFocused
      }
      .focusable(),
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
