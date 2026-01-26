package io.filmtime.feature.player

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    if (streamInfoJson == null) {
      PlayerError(message = "Missing stream information")
      return@composable
    }

    val streamInfo = try {
      val decoded = Uri.decode(streamInfoJson)
      Json.decodeFromString(StreamInfo.serializer(), decoded)
    } catch (e: Exception) {
      null
    }

    if (streamInfo == null) {
      PlayerError(message = "Invalid stream information")
      return@composable
    }

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

@Composable
private fun PlayerError(message: String) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = message)
  }
}
