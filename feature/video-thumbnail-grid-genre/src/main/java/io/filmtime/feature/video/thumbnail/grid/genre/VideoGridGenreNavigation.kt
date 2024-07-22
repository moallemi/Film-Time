package io.filmtime.feature.video.thumbnail.grid.genre

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable
import io.filmtime.data.model.VideoType

private const val ROUTE_PATH = "genre-grid"
private const val VIDEO_TYPE_ARG = "video_type"
private const val GENRE_ID_ARG = "genre_id"

fun NavGraphBuilder.videoThumbnailGridGenreScreen(
  rootRoute: DestinationRoute,
  onMovieClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onShowClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/$ROUTE_PATH/{$VIDEO_TYPE_ARG}/{${GENRE_ID_ARG}}",
    arguments = listOf(
      navArgument(GENRE_ID_ARG) {
        type = NavType.LongType
      },
      navArgument(VIDEO_TYPE_ARG) {
        type = NavType.EnumType(VideoType::class.java)
      },
    ),
  ) {
    VideoThumbnailGridByGenreScreen(
      onMovieClick = { onMovieClick(rootRoute, it) },
      onShowClick = { onShowClick(rootRoute, it) },
      onBack = onBack,
    )
  }
}

fun NavController.navigateVideoGridByGenre(
  rootRoute: DestinationRoute,
  genreId: Long,
  videoType: VideoType,
) {
  navigate("${rootRoute.route}/$ROUTE_PATH/$videoType/$genreId")
}
