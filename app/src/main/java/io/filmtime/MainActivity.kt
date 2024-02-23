package io.filmtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoType
import io.filmtime.feature.home.homeScreen
import io.filmtime.feature.movie.detail.movieDetailScreen
import io.filmtime.feature.movie.detail.navigateToMovieDetail
import io.filmtime.feature.movies.moviesScreen
import io.filmtime.feature.player.navigateToPlayer
import io.filmtime.feature.player.playerScreen
import io.filmtime.feature.show.detail.navigateToShowDetail
import io.filmtime.feature.show.detail.showDetailScreen
import io.filmtime.feature.shows.showsScreen
import io.filmtime.feature.trakt.login.navigateToTraktLogin
import io.filmtime.feature.trakt.login.traktLoginScreen
import io.filmtime.feature.video.thumbnail.grid.navigateToVideoThumbnailGridScreen
import io.filmtime.feature.video.thumbnail.grid.videoThumbnailGridScreen
import io.filmtime.ui.theme.FilmTimeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      FilmTimeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val navController = rememberNavController()
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
      }
    }
  }
}
