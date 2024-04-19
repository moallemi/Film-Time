package io.filmtime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoType
import io.filmtime.feature.movie.detail.movieDetailScreen
import io.filmtime.feature.movie.detail.navigateToMovieDetail
import io.filmtime.feature.player.navigateToPlayer
import io.filmtime.feature.player.playerScreen
import io.filmtime.feature.show.detail.navigateToShowDetail
import io.filmtime.feature.show.detail.showDetailScreen
import io.filmtime.feature.trakt.login.navigateToTraktLogin
import io.filmtime.feature.trakt.login.traktLoginScreen
import io.filmtime.feature.video.thumbnail.grid.navigateToVideoThumbnailGridScreen
import io.filmtime.feature.video.thumbnail.grid.videoThumbnailGridScreen

@Composable
fun FilmTimeNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  NavHost(navController = navController, startDestination = "home") {
    homeScreen(
      onMovieClick = navController::navigateToMovieDetail,
      onShowClick = navController::navigateToShowDetail,
      onTraktClick = navController::navigateToTraktLogin,
      onTrendingMoviesClick = {
        navController.navigateToVideoThumbnailGridScreen(
          videoType = VideoType.Movie,
          listType = VideoListType.Trending,
        )
      },
      onTrendingShowsClick = {
        navController.navigateToVideoThumbnailGridScreen(
          videoType = VideoType.Show,
          listType = VideoListType.Trending,
        )
      },
    )

    videoThumbnailGridScreen(
      onMovieClick = navController::navigateToMovieDetail,
      onBack = navController::popBackStack,
    )

    movieDetailScreen(
      onStreamReady = navController::navigateToPlayer,
      onCastItemClick = {},
      onMovieClick = navController::navigateToMovieDetail,
      onBack = navController::popBackStack,
    )

    showDetailScreen()

    playerScreen()

    moviesScreen(
      onMovieClick = navController::navigateToMovieDetail,
      onSectionClick = { listType ->
        navController.navigateToVideoThumbnailGridScreen(
          videoType = VideoType.Movie,
          listType = listType,
        )
      },
    )

    showsScreen(
      onShowClick = navController::navigateToShowDetail,
      onSectionClick = { listType ->
        navController.navigateToVideoThumbnailGridScreen(
          videoType = VideoType.Show,
          listType = listType,
        )
      },

      )

    traktLoginScreen(
      onBack = navController::popBackStack,
      onSuccess = navController::popBackStack,
    )
  }
}
