package io.filmtime.tv.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import io.filmtime.data.model.VideoThumbnail

@Composable
fun VerticalThumbnailCard(
  modifier: Modifier = Modifier,
  videoThumbnail: VideoThumbnail,
  onClick: () -> Unit = {},
) {
  var isFocused by remember {
    mutableStateOf(false)
  }
  var imageState by remember {
    mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
  }
  StandardCardContainer(
    modifier = Modifier
      .onFocusChanged {
        isFocused = it.isFocused
      }
      .then(modifier),
    imageCard = {
      Surface(
        onClick = onClick,
        shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.medium),
        glow = ClickableSurfaceDefaults.glow(
          focusedGlow = Glow(
            elevationColor = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
            12.dp,
          ),
        ),
        content = {
          AsyncImage(
            model = videoThumbnail.posterUrl,
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(CardDefaults.VerticalImageAspectRatio),
            contentScale = ContentScale.Crop,
            onState = {
              imageState = it
            },
            contentDescription = videoThumbnail.title,
          )
          AnimatedVisibility(
            visible = isFocused || imageState is AsyncImagePainter.State.Loading ||
              imageState is AsyncImagePainter.State.Error,
            enter = fadeIn(),
            exit = fadeOut(),
          ) {
            Box(
              modifier = Modifier
                .aspectRatio(CardDefaults.VerticalImageAspectRatio)
                .fillMaxWidth()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .3f),
                      MaterialTheme.colorScheme.surfaceVariant,
                    ),
                  ),
                ),
              contentAlignment = Alignment.Center,
            ) {
              Text(
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .padding(
                    bottom = 16.dp,
                    start = 12.dp,
                    end = 12.dp,
                  ),
                text = videoThumbnail.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
              )
            }
          }
        },
      )
    },
    title = {},
  )
}
