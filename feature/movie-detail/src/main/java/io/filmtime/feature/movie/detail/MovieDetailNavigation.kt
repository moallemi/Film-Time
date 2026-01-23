package io.filmtime.feature.movie.detail

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoType

fun NavGraphBuilder.movieDetailScreen(
  rootRoute: DestinationRoute,
  onStreamReady: (DestinationRoute, StreamInfo) -> Unit,
  onCastItemClick: (DestinationRoute, castId: Long) -> Unit,
  onMovieClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onGenreClick: (DestinationRoute, genreId: Long, genreName: String, videoType: VideoType) -> Unit,
  onNavigateToPluginManager: (DestinationRoute) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/movie_detail/{video_id}",
    screenName = "Movie Detail",
    arguments = listOf(
      navArgument("video_id") {
        type = NavType.IntType
      },
    ),
  ) {
    MovieDetailScreen(
      viewModel = hiltViewModel(),
      onGenreClick = { genre, type -> onGenreClick(rootRoute, genre.id, genre.name, type) },
      onStreamReady = { onStreamReady(rootRoute, it) },
      onCastItemClick = { onCastItemClick(rootRoute, it) },
      onMovieClick = { onMovieClick(rootRoute, it) },
      onBackPressed = onBack,
      onNavigateToPluginManager = { onNavigateToPluginManager(rootRoute) },
    )
  }
}

fun NavController.navigateToMovieDetail(
  rootRoute: DestinationRoute,
  tmdbId: Int,
) {
  navigate("${rootRoute.route}/movie_detail/$tmdbId")
}
