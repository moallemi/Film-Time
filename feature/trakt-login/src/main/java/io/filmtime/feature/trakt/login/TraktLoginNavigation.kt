package io.filmtime.feature.trakt.login

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.filmtime.core.ui.common.DestinationRoute

fun NavGraphBuilder.traktLoginScreen(
  rootRoute: DestinationRoute,
  onBack: () -> Unit,
  onSuccess: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/trakt/login",
  ) {
    TraktLoginWebView(
      viewModel = hiltViewModel(),
      onBackPressed = onBack,
      onSuccess = onSuccess,
    )
  }
}

fun NavController.navigateToTraktLogin(
  rootRoute: DestinationRoute,
) {
  navigate("${rootRoute.route}/trakt/login")
}
