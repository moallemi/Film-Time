package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.data.model.PreviewMovie
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoGenre

@Composable
fun VideoInfo(
  videoDetail: VideoDetail,
  modifier: Modifier = Modifier,
  onGenreClick: (VideoGenre) -> Unit,
) {
  Column(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(bottom = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
      style = MaterialTheme.typography.titleMedium,
      text = "Information",
    )
    GenresInfoItem(
      title = "Genres",
      genres = videoDetail.genres,
      onClick = onGenreClick,
    )
    InfoItem(
      title = "Release Date",
      subTitle = videoDetail.releaseDate,
    )
    InfoItem(
      title = "Spoken Languages",
      subTitle = videoDetail.spokenLanguages.joinToString { it },
    )
    videoDetail.budget?.let {
      InfoItem(
        title = "Budget",
        subTitle = "$$it",
      )
    }
    videoDetail.status?.let {
      InfoItem(
        title = "Status",
        subTitle = it,
      )
    }
  }
}

@Composable
private fun ColumnScope.InfoItem(
  title: String,
  subTitle: String,
) {
  Text(
    style = MaterialTheme.typography.bodyMedium,
    text = title,
  )
  Text(
    modifier = Modifier
      .padding(bottom = 8.dp),
    style = MaterialTheme.typography.bodySmall.copy(
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
    ),
    text = subTitle,
  )
}

@Composable
private fun ColumnScope.GenresInfoItem(
  title: String,
  genres: List<VideoGenre>,
  onClick: (VideoGenre) -> Unit,
) {
  Text(
    style = MaterialTheme.typography.bodyMedium,
    text = title,
  )
  LazyRow {
    items(items = genres) { genre ->
      Text(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .clickable { onClick(genre) }
          .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        ),
        text = genre.name,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun VideoAboutInfoPreview() {
  PreviewFilmTimeTheme {
    VideoInfo(
      videoDetail = VideoDetail.PreviewMovie,
      onGenreClick = {},
    )
  }
}
