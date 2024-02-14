package io.filmtime.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.homeScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onTraktClick: () -> Unit,
  onTrendingMoviesClick: () -> Unit,
  onTrendingShowsClick: () -> Unit,
) {
  composable("home") {
    HomeScreen(
      onMovieClick = onMovieClick,
      onShowClick = onShowClick,
      onTraktClick = onTraktClick,
      onTrendingMoviesClick = onTrendingMoviesClick,
      onTrendingShowsClick = onTrendingShowsClick,
    )
  }
}
