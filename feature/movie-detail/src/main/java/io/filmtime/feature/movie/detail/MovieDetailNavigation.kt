package io.filmtime.feature.movie.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.movieDetailScreen(
  onStreamReady: (streamUrl: String) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "detail/{video_id}",
    arguments = listOf(
      navArgument("video_id") {
        type = NavType.IntType
      },
    ),
  ) {
    MovieDetailScreen(
      viewModel = hiltViewModel(),
      onStreamReady = onStreamReady,
      onBackPressed = onBack,
    )
  }
}

fun NavController.navigateToMovieDetail(tmdbId: Int) {
  navigate("detail/$tmdbId")
}
