package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun VideoThumbnailPoster(
  posterUrl: String,
  height: Int,
  modifier: Modifier = Modifier,
) {
  AsyncImage(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(2 / 3f)
      .drawWithCache {
        val gradient = Brush.verticalGradient(
          colors = listOf(Color.Transparent, Color(0x99000000), Color(0xDD000000)),
          startY = height.toFloat() / 3,
          endY = height.toFloat(),
        )

        onDrawWithContent {
          drawContent()
          drawRect(gradient, blendMode = BlendMode.Multiply)
        }
      },
    contentScale = ContentScale.Crop,
    model = posterUrl,
    contentDescription = "Poster Image",
  )
}
