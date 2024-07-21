package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.data.model.Preview
import io.filmtime.data.model.PreviewMovie
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.VideoDetail

@Composable
fun VideoThumbnailInfo(
  videoDetail: VideoDetail,
  ratings: Ratings?,
  isBookmarked: Boolean,
  modifier: Modifier = Modifier,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
  primaryButton: @Composable () -> Unit,
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
        Text(text = videoDetail.genres.first().name)
        Text(text = "\u2022")
        Text(text = videoDetail.year.toString())
        videoDetail.runtime?.let { runtime ->
          Text(text = "\u2022")
          Text(text = runtime)
        }
        videoDetail.networks?.let { networks ->
          Text(text = "\u2022")
          Text(text = networks.firstOrNull().orEmpty())
        }
      }
    }

    primaryButton()

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

    ratings?.let {
      RatingsInfo(
        modifier = Modifier.fillMaxWidth(),
        ratings = ratings,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenPreview() {
  PreviewFilmTimeTheme {
    VideoThumbnailInfo(
      videoDetail = VideoDetail.PreviewMovie,
      isBookmarked = false,
      onAddBookmark = {},
      onRemoveBookmark = {},
      primaryButton = {
        FilmTimeFilledButton(
          modifier = Modifier
            .fillMaxWidth(),
          onClick = { },
        ) {
          Text("Play")
        }
      },
      ratings = Ratings.Preview,
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
