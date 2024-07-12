package io.filmtime.feature.video.thumbnail.grid

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoType

private const val ROUTE_PATH = "video-thumbnail-grid"
private const val VIDEO_TYPE_ARG = "videoType"
private const val LIST_TYPE_ARG = "listType"

fun NavGraphBuilder.videoThumbnailGridScreen(
  rootRoute: DestinationRoute,
  onMovieClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onShowClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/$ROUTE_PATH/{$VIDEO_TYPE_ARG}/{$LIST_TYPE_ARG}",
    arguments = listOf(
      navArgument(VIDEO_TYPE_ARG) {
        type = NavType.EnumType(VideoType::class.java)
      },
      navArgument(LIST_TYPE_ARG) {
        type = NavType.EnumType(VideoListType::class.java)
      },
    ),
  ) {
    VideoThumbnailGridScreen(
      onMovieClick = { onMovieClick(rootRoute, it) },
      onShowClick = { onShowClick(rootRoute, it) },
      onBack = onBack,
    )
  }
}

fun NavController.navigateToVideoThumbnailGridScreen(
  rootRoute: DestinationRoute,
  videoType: VideoType,
  listType: VideoListType,
) {
  navigate("${rootRoute.route}/$ROUTE_PATH/$videoType/$listType")
}

internal class VideoThumbnailGridArgs(
  val videoType: VideoType,
  val listType: VideoListType,
) {
  constructor(savedStateHandle: SavedStateHandle) : this(
    videoType = savedStateHandle[VIDEO_TYPE_ARG] ?: throw IllegalArgumentException("Video type not found"),
    listType = savedStateHandle[LIST_TYPE_ARG] ?: throw IllegalArgumentException("List type not found"),
  )
}
