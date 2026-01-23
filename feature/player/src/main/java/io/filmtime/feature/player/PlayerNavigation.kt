package io.filmtime.feature.player

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.data.model.StreamInfo
import kotlinx.serialization.json.Json

fun NavGraphBuilder.playerScreen(
  rootRoute: DestinationRoute,
) {
  composable(
    route = "${rootRoute.route}/player/{stream_info}",
    arguments = listOf(
      navArgument("stream_info") {
        type = NavType.StringType
      },
    ),
  ) { backStackEntry ->
    val streamInfoJson = backStackEntry.arguments?.getString("stream_info")
    val decoded = Uri.decode(streamInfoJson)
    val streamInfo = Json.decodeFromString(StreamInfo.serializer(), decoded)
    VideoPlayer(streamInfo = streamInfo)
  }
}

fun NavController.navigateToPlayer(
  rootRoute: DestinationRoute,
  streamInfo: StreamInfo,
) {
  val json = Json.encodeToString(StreamInfo.serializer(), streamInfo)
  val encoded = Uri.encode(json)
  navigate("${rootRoute.route}/player/$encoded")
}
