package io.filmtime.feature.show.detail.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Preview

@Composable
fun EpisodeThumbnailCard(
  modifier: Modifier = Modifier,
  episodeThumbnail: EpisodeThumbnail,
) {
  Card(
    modifier = modifier,
  ) {
    AsyncImage(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f),
      model = episodeThumbnail.posterUrl,
      contentDescription = episodeThumbnail.title,
      contentScale = ContentScale.Crop,
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, bottom = 4.dp)
        .padding(horizontal = 12.dp),
      text = "Episode ${episodeThumbnail.episodeNumber}",
      style = MaterialTheme.typography.labelMedium,
    )

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      text = episodeThumbnail.title,
    )

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp, bottom = 12.dp)
        .padding(horizontal = 12.dp),
      text = episodeThumbnail.description,
      style = MaterialTheme.typography.bodySmall,
      overflow = TextOverflow.Ellipsis,
      minLines = 3,
      maxLines = 3,
    )
  }
}

@ThemePreviews
@Composable
private fun Preview() {
  PreviewFilmTimeTheme {
    EpisodeThumbnailCard(
      episodeThumbnail = EpisodeThumbnail.Preview,
    )
  }
}
