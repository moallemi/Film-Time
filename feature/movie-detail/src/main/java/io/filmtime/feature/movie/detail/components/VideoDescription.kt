package io.filmtime.feature.movie.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.componnents.ExpandableText
import io.filmtime.data.model.Preview
import io.filmtime.data.model.VideoDetail

@Composable
fun VideoDescription(
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
      text = "About",
    )
    Card {
      videoDetail.tagline?.let {
        Text(
          modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
          style = MaterialTheme.typography.bodyLarge,
          text = videoDetail.tagline.orEmpty(),
          fontStyle = FontStyle.Italic,
        )
      }
      ExpandableText(
        modifier = Modifier
          .padding(16.dp),
        text = videoDetail.description,
        minimizedMaxLines = 5,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun VideoAboutInfoPreview() {
  PreviewFilmTimeTheme {
    VideoDescription(
      videoDetail = VideoDetail.Preview,
    )
  }
}
