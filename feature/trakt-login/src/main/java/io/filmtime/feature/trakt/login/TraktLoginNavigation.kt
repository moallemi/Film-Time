package io.filmtime.feature.trakt.login

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.traktLoginScreen(
  onBack: () -> Unit,
  onSuccess: () -> Unit,
) {
  composable(
    route = "trakt/login",
  ) {
    TraktLoginWebView(
      viewModel = hiltViewModel(),
      onBackPressed = onBack,
      onSuccess = onSuccess,
    )
  }
}

fun NavController.navigateToTraktLogin() {
  navigate("trakt/login")
}
