package io.filmtime.feature.video.thumbnail.grid.genre

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.filmtime.core.ui.common.componnents.VideoThumbnailGrid
import io.filmtime.data.model.VideoThumbnail

@Composable
fun VideoThumbnailGridByGenreScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  val viewModel = hiltViewModel<VideoGridGenreViewModel>()
  val pagedList = viewModel.pagedList.collectAsLazyPagingItems()

  VideoThumbnailGridByGenreScreen(
    pagedList = pagedList,
    onMovieClick = onMovieClick,
    onShowClick = onShowClick,
    onBack = onBack,
  )
}

@Composable
private fun VideoThumbnailGridByGenreScreen(
  pagedList: LazyPagingItems<VideoThumbnail>,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
    },
    content = { padding ->
      VideoThumbnailGrid(
        contentPadding = padding,
        pagedList = pagedList,
        onMovieClick = onMovieClick,
        onShowClick = onShowClick,
      )
    },
  )
}
