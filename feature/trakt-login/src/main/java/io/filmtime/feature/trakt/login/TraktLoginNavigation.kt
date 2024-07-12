package io.filmtime.feature.trakt.login

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import io.filmtime.core.browser.openUrl
import io.filmtime.core.ui.navigation.DestinationRoute

fun NavGraphBuilder.traktLoginScreen(
  rootRoute: DestinationRoute,
  onBack: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/trakt/auth?code={code}&error={error}",
    deepLinks = listOf(
      navDeepLink {
        uriPattern = "filmtime://trakt/auth?code={code}&error={error}"
        action = Intent.ACTION_VIEW
      },
    ),
    arguments = listOf(
      navArgument("code") {
        type = NavType.StringType
        nullable = true
      },
      navArgument("error") {
        type = NavType.StringType
        nullable = true
      },
    ),
  ) {
    TraktLoginScreen(
      onBackClick = onBack,
    )
  }
}

fun NavController.navigateToTraktLogin() {
  context.openUrl(TRAKT_LOGIN_URL, isExternal = false)
}

private const val TRAKT_LOGIN_URL =
  "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=" +
    "${BuildConfig.TRAKT_CLIENT_ID}&redirect_uri=filmtime://trakt/auth"
