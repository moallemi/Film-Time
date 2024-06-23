package io.filmtime.feature.show.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.filmtime.core.ui.common.DestinationRoute

fun NavGraphBuilder.showDetailScreen(
  rootRoute: DestinationRoute,
  onStreamReady: (DestinationRoute, streamUrl: String) -> Unit,
  onCastItemClick: (DestinationRoute, castId: Long) -> Unit,
  onSimilarClick: (DestinationRoute, tmdbId: Int) -> Unit,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/show_detail/{video_id}",
    arguments = listOf(
      navArgument("video_id") {
        type = NavType.IntType
      },
    ),
  ) {
    ShowDetailScreen(
      viewModel = hiltViewModel(),
      onCastItemClick = { onCastItemClick(rootRoute, it) },
      onShowClick = { onSimilarClick(rootRoute, it) },
      onBackPressed = onBack,
    )
  }
}

fun NavController.navigateToShowDetail(
  rootRoute: DestinationRoute,
  tmdbId: Int,
) {
  navigate("${rootRoute.route}/show_detail/$tmdbId")
}
