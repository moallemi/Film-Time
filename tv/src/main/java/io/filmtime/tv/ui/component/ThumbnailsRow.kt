package io.filmtime.tv.ui.component

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ThumbnailsRow(
  modifier: Modifier = Modifier,
  thumbnails: List<VideoThumbnail>,
  title: String,
  onClick: (tmdbId: Int, type: VideoType) -> Unit = { _, _ -> },
) {
  val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }

  Column(modifier.focusGroup()) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(
        horizontal = 60.dp,
        vertical = 20.dp,
      ),
    )
    LazyRow(
      modifier = Modifier
        .focusRequester(lazyRow)
        .focusRestorer { firstItem },
      contentPadding = PaddingValues(
        horizontal = 60.dp,
      ),
      horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
      itemsIndexed(
        thumbnails,
        key = { _, video -> video.posterUrl },
      ) { index, item ->
        val itemModifier = if (index == 0) {
          Modifier.focusRequester(firstItem)
        } else {
          Modifier
        }
        VerticalThumbnailCard(
          videoThumbnail = item,
          modifier = itemModifier.width(150.dp),
          onClick = { item.ids.tmdbId?.let { onClick(it, item.type) } },
        )
      }
    }
  }
}
