package io.filmtime.feature.show.detail.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
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
import io.filmtime.core.ui.common.componnents.placeholder.PlaceholderHighlight
import io.filmtime.core.ui.common.componnents.placeholder.fade
import io.filmtime.core.ui.common.componnents.placeholder.placeholder
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Preview

@Composable
fun EpisodeThumbnailCard(
  modifier: Modifier = Modifier,
  episodeThumbnail: EpisodeThumbnail,
  placeHolderVisible: Boolean = false,
) {
  if (placeHolderVisible) {
    ElevatedCard(
      modifier = modifier,
    ) {
      CardContent(
        episodeThumbnail = episodeThumbnail,
        placeHolderVisible = placeHolderVisible,
      )
    }
  } else {
    Card(
      modifier = modifier,
    ) {
      CardContent(
        episodeThumbnail = episodeThumbnail,
        placeHolderVisible = placeHolderVisible,
      )
    }
  }
}

@Composable
private fun CardContent(
  episodeThumbnail: EpisodeThumbnail,
  placeHolderVisible: Boolean,
) {
  val placeholderHighlight = PlaceholderHighlight.fade()
  AsyncImage(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(16f / 9f)
      .placeholder(
        visible = placeHolderVisible,
        highlight = placeholderHighlight,
      ),
    model = episodeThumbnail.posterUrl,
    contentDescription = episodeThumbnail.title,
    contentScale = ContentScale.Crop,
  )
  Text(
    modifier = Modifier
      .padding(top = 8.dp, bottom = 4.dp)
      .padding(horizontal = 12.dp)
      .placeholder(
        visible = placeHolderVisible,
        highlight = placeholderHighlight,
      ),
    text = "Episode ${episodeThumbnail.episodeNumber}",
    style = MaterialTheme.typography.labelMedium,
  )

  Text(
    modifier = Modifier
      .padding(horizontal = 12.dp)
      .placeholder(
        visible = placeHolderVisible,
        highlight = placeholderHighlight,
      ),
    text = episodeThumbnail.title,
  )

  Text(
    modifier = Modifier
      .padding(top = 4.dp, bottom = 12.dp)
      .padding(horizontal = 12.dp)
      .placeholder(
        visible = placeHolderVisible,
        highlight = placeholderHighlight,
      ),
    text = episodeThumbnail.description,
    style = MaterialTheme.typography.bodySmall,
    overflow = TextOverflow.Ellipsis,
    minLines = 3,
    maxLines = 3,
  )
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

@ThemePreviews
@Composable
private fun PreviewLoading() {
  PreviewFilmTimeTheme {
    EpisodeThumbnailCard(
      episodeThumbnail = EpisodeThumbnail.Preview,
      placeHolderVisible = true,
    )
  }
}
