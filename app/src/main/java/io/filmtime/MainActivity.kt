package io.filmtime

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.filmtime.feature.home.HomeScreen
import io.filmtime.feature.movie.detail.MovieDetailScreen
import io.filmtime.feature.player.VideoPlayer
import io.filmtime.feature.show.detail.ShowDetailScreen
import io.filmtime.feature.trakt.login.TraktLoginWebView
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
            composable("home") {
              HomeScreen(
                viewModel = hiltViewModel(),
                onMovieClick = { tmdbId ->
                  navController.navigate("detail/$tmdbId")
                },
                onShowClick = { tmdbId ->
                  navController.navigate("show/detail/$tmdbId")
                },
                onTraktClick = {
                  navController.navigate("trakt/login")
                },
              )
            }
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
                onStreamReady = { streamUrl ->
                  val encoded = Uri.encode(streamUrl)
                  navController.navigate("player/$encoded")
                },
                onBackPressed = {
                  navController.popBackStack()
                },
              )
            }
            composable(
              route = "player/{stream_url}",
              arguments = listOf(
                navArgument("stream_url") {
                  type = NavType.StringType
                },
              ),
            ) { backStackEntry ->
              val streamUrl = backStackEntry.arguments?.getString("stream_url")
              val decoded = Uri.decode(streamUrl)
              VideoPlayer(uri = Uri.parse(decoded))
            }

            composable(
              route = "show/detail/{video_id}",
              arguments = listOf(
                navArgument("video_id") {
                  type = NavType.IntType
                },
              ),
            ) {
              ShowDetailScreen(
                viewModel = hiltViewModel(),
              )
            }
            composable(
              route = "trakt/login",
            ) {
              TraktLoginWebView(
                viewModel = hiltViewModel(),
                onBackPressed = {
                  navController.popBackStack()
                },
                onSuccess = {
                  navController.popBackStack()
                },
              )
            }
          }
        }
      }
    }
  }
}
