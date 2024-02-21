package io.filmtime.feature.movies

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.filmtime.data.model.VideoListType

fun NavGraphBuilder.moviesScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onSectionClick: (videoListType: VideoListType) -> Unit,
) {
  composable("movies") {
    MoviesScreen(
      onMovieClick = onMovieClick,
      onSectionClick = onSectionClick,
    )
  }
}
