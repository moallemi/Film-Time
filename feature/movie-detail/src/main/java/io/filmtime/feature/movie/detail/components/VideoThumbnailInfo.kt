package io.filmtime.feature.movie.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.composable.FilmTimeFilledButton
import io.filmtime.core.designsystem.composable.FilmTimeFilledTonalButton
import io.filmtime.core.designsystem.composable.FilmTimeOutlinedButton
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.componnents.ExpandableText
import io.filmtime.data.model.Preview
import io.filmtime.data.model.VideoDetail

@Composable
internal fun VideoThumbnailInfo(
  videoDetail: VideoDetail,
  isBookmarked: Boolean,
  modifier: Modifier = Modifier,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
  traktHistoryButton: @Composable RowScope.() -> Unit,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Text(
      modifier = Modifier.fillMaxWidth(),
      text = videoDetail.title,
      style = MaterialTheme.typography.headlineLarge,
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterHorizontally),
    ) {
      ProvideTextStyle(
        MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurface,
        ),
      ) {
        Text(text = videoDetail.genres.firstOrNull().orEmpty())
        Text(text = "\u2022")
        Text(text = videoDetail.year.toString())
        Text(text = "\u2022")
        Text(text = videoDetail.runtime.orEmpty())
      }
    }
    FilmTimeFilledButton(
      modifier = Modifier
        .fillMaxWidth(),
      onClick = {},
    ) {
      Text("Play")
    }
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      traktHistoryButton()
      BookmarkButton(
        modifier = Modifier
          .weight(1f),
        isBookmarked = isBookmarked,
        onRemoveBookmark = onRemoveBookmark,
        onAddBookmark = onAddBookmark,
      )
    }
    ExpandableText(
      text = videoDetail.description,
      minimizedMaxLines = 3,
    )
  }
}

@Composable
private fun BookmarkButton(
  isBookmarked: Boolean,
  onRemoveBookmark: () -> Unit,
  onAddBookmark: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (isBookmarked) {
    FilmTimeOutlinedButton(
      modifier = modifier,
      onClick = onRemoveBookmark,
    ) {
      Icon(
        Filled.Check,
        contentDescription = "",
        modifier = Modifier.size(20.dp),
      )
      Spacer(modifier = Modifier.size(8.dp))
      Text("Bookmarked")
    }
  } else {
    FilmTimeFilledTonalButton(
      modifier = modifier,
      onClick = onAddBookmark,
    ) {
      Icon(
        Filled.Add,
        contentDescription = "",
        modifier = Modifier.size(20.dp),
      )
      Spacer(modifier = Modifier.size(8.dp))
      Text("Bookmark")
    }
  }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenPreview() {
  PreviewFilmTimeTheme {
    VideoThumbnailInfo(
      videoDetail = VideoDetail.Preview,
      isBookmarked = false,
      onAddBookmark = {},
      onRemoveBookmark = {},
      traktHistoryButton = {
        FilmTimeFilledTonalButton(
          modifier = Modifier
            .weight(1f),
          onClick = {},
        ) {
          Text("Add to history")
        }
      },
    )
  }
}
