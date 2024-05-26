package io.filmtime.core.ui.common.componnents

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.AutoMirrored.Rounded
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoSectionRow(
  title: String,
  items: List<VideoThumbnail>,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onSectionClick: (() -> Unit)?,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    Header(
      onSectionClick = onSectionClick,
      title = title,
    )
    LazyRow(
      modifier = Modifier
        .height(180.dp)
        .fillMaxWidth(),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(items) { item ->
        VideoThumbnailCard(
          modifier = Modifier
            .testTag("discover_carousel_item")
            .animateItemPlacement()
            .fillParentMaxHeight()
            .aspectRatio(2 / 3f),
          videoThumbnail = item,
          onClick = {
            item.ids.tmdbId?.let {
              when (item.type) {
                VideoType.Movie -> onMovieClick(it)
                VideoType.Show -> onShowClick(it)
              }
            } ?: run {
              Log.e("VideoSectionRow", "tmdbId is null")
            }
          },
        )
      }
    }
  }
}

@Composable
private fun Header(
  onSectionClick: (() -> Unit)?,
  title: String,
) {
  val isClickable = onSectionClick != null
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(isClickable) { onSectionClick?.invoke() }
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      modifier = Modifier
        .weight(1f),
      text = title,
      style = MaterialTheme.typography.titleMedium,
    )
    if (isClickable) {
      Icon(
        imageVector = Rounded.ArrowForward,
        contentDescription = "Open section",
      )
    }
  }
}
