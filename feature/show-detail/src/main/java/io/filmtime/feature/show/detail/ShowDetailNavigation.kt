package io.filmtime.feature.show.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoType

fun NavGraphBuilder.showDetailScreen(
  rootRoute: DestinationRoute,
  onStreamReady: (DestinationRoute, StreamInfo) -> Unit,
  onCastItemClick: (DestinationRoute, castId: Long) -> Unit,
  onSimilarClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onGenreClick: (DestinationRoute, genreId: Long, genreName: String, type: VideoType) -> Unit,
  onNavigateToPluginManager: (DestinationRoute) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/show_detail/{video_id}",
    screenName = "Show Detail",
    arguments = listOf(
      navArgument("video_id") {
        type = NavType.IntType
      },
    ),
  ) {
    ShowDetailScreen(
      onCastItemClick = { onCastItemClick(rootRoute, it) },
      onShowClick = { onSimilarClick(rootRoute, it) },
      onGenreClick = { genre, type -> onGenreClick(rootRoute, genre.id, genre.name, type) },
      onBackPressed = onBack,
      onStreamReady = { onStreamReady(rootRoute, it) },
      onNavigateToPluginManager = { onNavigateToPluginManager(rootRoute) },
    )
  }
}

fun NavController.navigateToShowDetail(
  rootRoute: DestinationRoute,
  tmdbId: Int,
) {
  navigate("${rootRoute.route}/show_detail/$tmdbId")
}
