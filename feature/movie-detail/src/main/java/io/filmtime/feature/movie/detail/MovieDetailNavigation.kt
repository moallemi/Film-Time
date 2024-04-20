package io.filmtime.feature.movie.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.filmtime.core.ui.common.DestinationRoute

fun NavGraphBuilder.movieDetailScreen(
  rootRoute: DestinationRoute,
  onStreamReady: (DestinationRoute, streamUrl: String) -> Unit,
  onCastItemClick: (DestinationRoute, castId: Long) -> Unit,
  onMovieClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/detail/{video_id}",
    arguments = listOf(
      navArgument("video_id") {
        type = NavType.IntType
      },
    ),
  ) {
    MovieDetailScreen(
      viewModel = hiltViewModel(),
      onStreamReady = { onStreamReady(rootRoute, it) },
      onCastItemClick = { onCastItemClick(rootRoute, it) },
      onMovieClick = { onMovieClick(rootRoute, it) },
      onBackPressed = onBack,
    )
  }
}

fun NavController.navigateToMovieDetail(
  rootRoute: DestinationRoute,
  tmdbId: Int,
) {
  navigate("${rootRoute.route}/detail/$tmdbId")
}
