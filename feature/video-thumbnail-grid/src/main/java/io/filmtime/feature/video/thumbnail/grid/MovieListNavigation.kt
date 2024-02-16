package io.filmtime.feature.video.thumbnail.grid

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.videoThumbnailGridScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable("video-thumbnail-grid") {
    VideoThumbnailGridScreen(
      onMovieClick = onMovieClick,
      onBack = onBack,
    )
  }
}

fun NavController.navigateToVideoThumbnailGridScreen() {
  navigate("video-thumbnail-grid")
}
