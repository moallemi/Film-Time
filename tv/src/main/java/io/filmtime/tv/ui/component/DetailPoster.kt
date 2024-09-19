package io.filmtime.tv.ui.component

import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import io.filmtime.tv.R

@Composable
fun DetailPoster(
  coverUrl: String,
  modifier: Modifier = Modifier,
  scrimColor: Color = MaterialTheme.colorScheme.surface,
) {
  SubcomposeAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(coverUrl).crossfade(true).build(),
    contentDescription = stringResource(R.string.cd_poster_image),
    contentScale = ContentScale.Crop,
    loading = { PosterLoadingPlaceholder(modifier = Modifier.fillMaxSize()) },
    error = { PosterErrorPlaceholder(modifier = Modifier.fillMaxSize()) },
    modifier = modifier.drawWithContent {
      drawContent()
      drawRect(
        Brush.verticalGradient(
          colors = listOf(Color.Transparent, scrimColor),
          startY = 600f,
        ),
      )
      drawRect(
        Brush.horizontalGradient(
          colors = listOf(scrimColor, Color.Transparent),
          endX = 1000f,
          startX = 300f,
        ),
      )
      drawRect(
        Brush.linearGradient(
          colors = listOf(scrimColor, Color.Transparent),
          start = Offset(x = 500f, y = 500f),
          end = Offset(x = 1000f, y = 0f),
        ),
      )
    },

  )
}

@Composable
fun PosterLoadingPlaceholder(
  modifier: Modifier = Modifier,
  durationInMillis: Int = 1000,
  scrimColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
  val infiniteTransition = rememberInfiniteTransition(label = "Poster loading transition")
  val alphaAnimation by infiniteTransition.animateFloat(
    initialValue = .1f,
    targetValue = .5f,
    animationSpec = infiniteRepeatable(
      repeatMode = Reverse,
      animation = tween(durationInMillis),
    ),
    label = "Placeholder fadeIn/fadeOut animation",
  )
  Box(
    modifier = modifier
      .drawBehind {
        drawRect(
          brush = Brush.linearGradient(
            colors = listOf(scrimColor, Color.Transparent),
            start = Offset(size.width, 0f),
            end = Offset(size.width / 3, size.height / 2),
          ),
        )
      }
      .padding(30.dp),
  ) {
    Icon(
      modifier = Modifier
        .size(200.dp)
        .align(Alignment.TopEnd)
        .graphicsLayer {
          alpha = alphaAnimation
        },
      imageVector = Icons.Rounded.Movie,
      contentDescription = null,
    )
  }
}

@Composable
private fun PosterErrorPlaceholder(
  modifier: Modifier = Modifier,
  scrimColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
  val crossLineColor = MaterialTheme.colorScheme.onSurfaceVariant
  Box(
    modifier = modifier
      .drawBehind {
        drawRect(
          brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, scrimColor),
            start = Offset(size.width, 0f),
            end = Offset(size.width / 3, size.height / 2),
          ),
        )
      }
      .padding(30.dp),
  ) {
    Icon(
      modifier = Modifier
        .size(200.dp)
        .align(Alignment.TopEnd)
        .alpha(.3f)
        .drawWithContent {
          drawContent()
          drawLine(
            color = crossLineColor,
            strokeWidth = 8.dp.toPx(),
            cap = StrokeCap.Round,
            start = Offset(50f, 50f),
            end = Offset(size.width - 50, size.height - 50),
          )
        },
      imageVector = Icons.Rounded.Movie,
      contentDescription = null,
    )
  }
}
