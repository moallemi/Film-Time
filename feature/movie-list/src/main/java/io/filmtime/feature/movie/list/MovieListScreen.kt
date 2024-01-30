package io.filmtime.feature.movie.list

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.filmtime.core.designsystem.plus
import io.filmtime.core.ui.common.componnents.VideoThumbnailCard
import io.filmtime.data.model.VideoThumbnail

@Composable
fun MovieListScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  val viewModel = hiltViewModel<MovieListViewModel>()
  val pagedList = viewModel.pagedList.collectAsLazyPagingItems()

  MovieListScreen(
    pagedList = pagedList,
    onMovieClick = onMovieClick,
    onBack = onBack,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListScreen(
  pagedList: LazyPagingItems<VideoThumbnail>,
  onMovieClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(title = { Text(text = "Popular Movies") })
    },
    content = { padding ->
      MovieListContent(
        contentPadding = padding,
        pagedList = pagedList,
        onMovieClick = onMovieClick,
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
      val movie = pagedList[index]
      if (movie != null) {
        VideoThumbnailCard(
          modifier = Modifier
            .animateItemPlacement()
            .width(200.dp),
          videoThumbnail = movie,
          onClick = {
            movie.ids.tmdbId?.let {
              onMovieClick(it)
            } ?: run {
              Log.e("MovieListScreen", "tmdbId is null")
            }
          },
        )
      }
    }
  }
}
