package io.filmtime.feature.movie.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.data.model.Preview
import io.filmtime.data.model.VideoDetail

@Composable
fun VideoInfo(
  videoDetail: VideoDetail,
  modifier: Modifier = Modifier,
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
    InfoItem(
      title = "Genres",
      subTitle = videoDetail.genres.joinToString { it },
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
    style = MaterialTheme.typography.bodySmall.copy(
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
    ),
    text = subTitle,
  )
}

@ThemePreviews
@Composable
private fun VideoAboutInfoPreview() {
  PreviewFilmTimeTheme {
    VideoInfo(
      videoDetail = VideoDetail.Preview,
    )
  }
}
