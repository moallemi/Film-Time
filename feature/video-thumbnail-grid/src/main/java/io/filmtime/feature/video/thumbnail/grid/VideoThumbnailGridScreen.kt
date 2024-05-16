package io.filmtime.feature.video.thumbnail.grid

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.filmtime.core.designsystem.plus
import io.filmtime.core.ui.common.componnents.VideoThumbnailCard
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType

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
      MovieListContent(
        contentPadding = padding,
        pagedList = pagedList,
        onMovieClick = onMovieClick,
        onShowClick = onShowClick,
      )
    },
  )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieListContent(
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
      key = { index -> pagedList[index]?.ids?.tmdbId ?: index },
    ) { index ->
      val videoThumbnail = pagedList[index]
      if (videoThumbnail != null) {
        VideoThumbnailCard(
          modifier = Modifier
            .animateItemPlacement()
            .width(200.dp),
          videoThumbnail = videoThumbnail,
          onClick = {
            videoThumbnail.ids.tmdbId?.let {
              when (videoThumbnail.type) {
                VideoType.Movie -> onMovieClick(it)
                VideoType.Show -> onShowClick(it)
              }
            } ?: run {
              Log.e("VideoThumbnailGridScreen", "tmdbId is null")
            }
          },
        )
      }
    }
  }
}
