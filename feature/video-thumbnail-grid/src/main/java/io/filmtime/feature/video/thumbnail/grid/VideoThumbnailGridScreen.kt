package io.filmtime.feature.video.thumbnail.grid

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.filmtime.core.ui.common.componnents.VideoThumbnailGrid
import io.filmtime.data.model.VideoThumbnail

@Composable
fun VideoThumbnailGridScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  val viewModel = hiltViewModel<VideoThumbnailGridViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()
  val pagedList = viewModel.pagedList.collectAsLazyPagingItems()

  VideoThumbnailGridScreen(
    state = state,
    pagedList = pagedList,
    onMovieClick = onMovieClick,
    onShowClick = onShowClick,
    onBack = onBack,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoThumbnailGridScreen(
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
      TopAppBar(
        title = { Text(text = state.title) },
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
