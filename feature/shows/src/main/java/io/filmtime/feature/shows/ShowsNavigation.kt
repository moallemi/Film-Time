package io.filmtime.feature.shows

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.filmtime.data.model.VideoListType

fun NavGraphBuilder.showsScreen(
  onShowClick: (tmdbId: Int) -> Unit,
  onSectionClick: (videoListType: VideoListType) -> Unit,
) {
  composable("shows") {
    ShowsScreen(
      onShowClick = onShowClick,
      onSectionClick = onSectionClick,
    )
  }
}
