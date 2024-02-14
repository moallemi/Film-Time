package io.filmtime.feature.movie.list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.movieListScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable("movie-list") {
    MovieListScreen(
      onMovieClick = onMovieClick,
      onBack = onBack,
    )
  }
}

fun NavController.navigateToMovieList() {
  navigate("movie-list")
}
