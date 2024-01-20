package io.filmtime.feature.show.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.showDetailScreen() {
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
}

fun NavController.navigateToShowDetail(tmdbId: Int) {
  navigate("show/detail/$tmdbId")
}
