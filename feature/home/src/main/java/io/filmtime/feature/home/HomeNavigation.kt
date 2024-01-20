package io.filmtime.feature.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.homeScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onTraktClick: () -> Unit,
) {
  composable("home") {
    HomeScreen(
      viewModel = hiltViewModel(),
      onMovieClick = onMovieClick,
      onShowClick = onShowClick,
      onTraktClick = onTraktClick,
    )
  }
}
