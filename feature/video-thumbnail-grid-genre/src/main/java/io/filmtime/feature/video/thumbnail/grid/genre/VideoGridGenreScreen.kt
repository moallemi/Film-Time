package io.filmtime.feature.video.thumbnail.grid.genre

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        title = {},
        navigationIcon = {
          IconButton(
            onClick = onBack,
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
              contentDescription = "Back",
            )
          }
        },
        scrollBehavior = scrollBehavior,
      )
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
