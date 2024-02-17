package io.filmtime.feature.video.thumbnail.grid

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoType

fun NavGraphBuilder.videoThumbnailGridScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "$ROUTE_PATH/{$VIDEO_TYPE_ARG}/{$LIST_TYPE_ARG}",
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
      onMovieClick = onMovieClick,
      onBack = onBack,
    )
  }
}

fun NavController.navigateToVideoThumbnailGridScreen(
  videoType: VideoType,
  listType: VideoListType,
) {
  navigate("$ROUTE_PATH/$videoType/$listType")
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

internal const val ROUTE_PATH = "video-thumbnail-grid"
internal const val VIDEO_TYPE_ARG = "videoType"
internal const val LIST_TYPE_ARG = "listType"
