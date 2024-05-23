package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType.Show

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
  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    Text(
      modifier = Modifier
        .align(Alignment.Center)
        .padding(8.dp),
      text = videoThumbnail.title,
      style = MaterialTheme.typography.bodySmall,
      textAlign = TextAlign.Center,
    )
    AsyncImage(
      model = videoThumbnail.posterUrl,
      contentDescription = videoThumbnail.title,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )
  }
}

@ThemePreviews
@Composable
private fun VideoThumbnailCardPreview() {
  PreviewFilmTimeTheme {
    VideoThumbnailCard(
      videoThumbnail = VideoThumbnail(
        ids = VideoId(traktId = null, tmdbId = null),
        title = "Dark Matter",
        posterUrl = "",
        year = 2001,
        type = Show,
      ),
      onClick = {},
    )
  }
}
