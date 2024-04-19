package io.filmtime.ui.navigation

sealed class RootScreen(val destinationRoute: String) {
  data object Home : RootScreen("home")
  data object Movies : RootScreen("movies")
  data object Shows : RootScreen("shows")
  data object Settings : RootScreen("settings")
}
