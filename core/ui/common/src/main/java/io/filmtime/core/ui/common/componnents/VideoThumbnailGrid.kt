package io.filmtime.core.ui.common.componnents

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import io.filmtime.core.designsystem.plus
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoThumbnailGrid(
  contentPadding: PaddingValues,
  pagedList: LazyPagingItems<VideoThumbnail>,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(100.dp),
    modifier = Modifier,
    contentPadding = contentPadding + PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(
      pagedList.itemCount,
      key = pagedList.itemKey { item -> item.ids.tmdbId!! }, // probable source of crash if we do not use tmdb as source
    ) { index ->
      val videoThumbnail = pagedList[index]
      if (videoThumbnail != null) {
        VideoThumbnailCard(
          modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .aspectRatio(2 / 3f),
          videoThumbnail = videoThumbnail,
          onClick = {
            videoThumbnail.ids.tmdbId?.let {
              when (videoThumbnail.type) {
                VideoType.Movie -> onMovieClick(it)
                VideoType.Show -> onShowClick(it)
              }
            } ?: run {
              Log.e("VideoThumbnailGrid", "tmdbId is null")
            }
          },
        )
      }
    }
  }
}
