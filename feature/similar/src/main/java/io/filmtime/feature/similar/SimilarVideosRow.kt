package io.filmtime.feature.similar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.ui.common.componnents.VideoSectionRow
import io.filmtime.data.model.VideoType

@Composable
fun SimilarVideosRow(
  modifier: Modifier = Modifier,
  tmdbId: Int,
  videoType: VideoType,
  onVideoClick: (Int) -> Unit,
) {
  val viewModel = hiltViewModel<SimilarViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(tmdbId, videoType) {
    viewModel.loadSimilar(tmdbId, videoType)
  }

  VideoSectionRow(
    modifier = modifier,
    isLoading = state.isLoading,
    error = state.error,
    title = "Similar",
    items = state.videoItems,
    onMovieClick = onVideoClick,
    onShowClick = onVideoClick,
    onSectionClick = null,
    onRetryClick = {
      viewModel.loadSimilar(tmdbId, videoType)
    },
  )
}
