package io.filmtime.feature.video.thumbnail.grid.genre

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.filmtime.core.designsystem.composable.FilmTimeSmallTopAppBar
import io.filmtime.core.ui.common.componnents.VideoThumbnailGrid
import io.filmtime.data.model.VideoThumbnail

@Composable
internal fun VideoThumbnailGridByGenreScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  val viewModel = hiltViewModel<VideoGridGenreViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()
  val pagedList = viewModel.pagedList.collectAsLazyPagingItems()

  VideoThumbnailGridByGenreScreen(
    state = state,
    pagedList = pagedList,
    onMovieClick = onMovieClick,
    onShowClick = onShowClick,
    onBack = onBack,
  )
}

@Composable
private fun VideoThumbnailGridByGenreScreen(
  state: VideoThumbnailGridUiState,
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
      FilmTimeSmallTopAppBar(
        title = state.title,
        navigationIcon = Icons.AutoMirrored.Rounded.ArrowBack,
        scrollBehavior = scrollBehavior,
        onNavClick = onBack,
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
