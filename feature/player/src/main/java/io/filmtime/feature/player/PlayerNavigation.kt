package io.filmtime.feature.player

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.filmtime.core.ui.navigation.DestinationRoute

fun NavGraphBuilder.playerScreen(
  rootRoute: DestinationRoute,
) {
  composable(
    route = "${rootRoute.route}/player/{stream_url}",
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
}

fun NavController.navigateToPlayer(
  rootRoute: DestinationRoute,
  streamUrl: String,
) {
  val encoded = Uri.encode(streamUrl)
  navigate("${rootRoute.route}/player/$encoded")
}
