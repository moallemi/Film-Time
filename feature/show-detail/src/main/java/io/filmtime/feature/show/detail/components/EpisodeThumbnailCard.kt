package io.filmtime.feature.show.detail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import io.filmtime.feature.show.detail.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeThumbnailCard(
  modifier: Modifier = Modifier,
  episodeThumbnail: EpisodeThumbnail,
  placeHolderVisible: Boolean,
  onClick: (EpisodeThumbnail) -> Unit,
  addToHistory: (EpisodeThumbnail) -> Unit,
  removeFromHistory: (EpisodeThumbnail) -> Unit,
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
    var expanded by remember { mutableStateOf(false) }
    Card(
      modifier = modifier
        .combinedClickable(
          onClick = { onClick(episodeThumbnail) },
          onLongClick = { expanded = true },
        ),
    ) {
      CardContent(
        episodeThumbnail = episodeThumbnail,
        placeHolderVisible = placeHolderVisible,
      )
      DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
      ) {
        DropdownMenuItem(
          onClick = {
            if (episodeThumbnail.isWatched) {
              removeFromHistory(episodeThumbnail)
            } else {
              addToHistory(episodeThumbnail)
            }
            expanded = false
          },
          text = {
            Text(text = if (episodeThumbnail.isWatched) "Mark As Unwatched" else "Mark As Watched")
          },
        )
      }
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
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 8.dp, bottom = 4.dp)
      .padding(horizontal = 12.dp)
      .height(24.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      modifier = Modifier
        .placeholder(
          visible = placeHolderVisible,
          highlight = placeholderHighlight,
        ),
      text = "Episode ${episodeThumbnail.episodeNumber}",
      style = MaterialTheme.typography.labelMedium,
    )
    Spacer(modifier = Modifier.weight(1f))
    if (episodeThumbnail.isWatched) {
      Icon(
        modifier = Modifier
          .placeholder(
            visible = placeHolderVisible,
            highlight = placeholderHighlight,
          ),
        painter = painterResource(R.drawable.rectangle_badge_checkmark),
        contentDescription = "Watched",
      )
    }
  }

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
      placeHolderVisible = false,
      onClick = {},
      addToHistory = {},
      removeFromHistory = {},
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
      onClick = {},
      addToHistory = {},
      removeFromHistory = {},
    )
  }
}
