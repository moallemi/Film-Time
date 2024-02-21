package io.filmtime.feature.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.ui.common.componnents.LoadingVideoSectionRow
import io.filmtime.core.ui.common.componnents.VideoSectionRow
import io.filmtime.data.model.VideoListType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onSectionClick: (VideoListType) -> Unit,
) {
  val viewModel = hiltViewModel<MoviesViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Movies") },
      )
    },
  ) {
    Box(modifier = Modifier.padding(it)) {
      if (state.isLoading) {
        LoadingVideoSectionRow(numberOfSections = 4)
      } else {
        LazyColumn(
          contentPadding = PaddingValues(top = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          items(state.videoSections) { videoSection ->
            VideoSectionRow(
              title = videoSection.title,
              items = videoSection.items,
              onMovieClick = onMovieClick,
              onShowClick = { throw IllegalStateException("Shows not supported") },
              onSectionClick = { onSectionClick(videoSection.type) },
            )
          }
        }
      }
    }
  }
}
