package io.filmtime.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

private const val SETTINGS_GRAPH_ROUTE = "settings_graph"
private const val SETTINGS_SCREEN_ROUTE = "settings"

fun NavGraphBuilder.settingsGraph() {
  navigation(
    route = SETTINGS_GRAPH_ROUTE,
    startDestination = SETTINGS_SCREEN_ROUTE,
  ) {
    composable(
      route = SETTINGS_SCREEN_ROUTE,
    ) {
      SettingsScreen()
    }
  }
}

fun NavController.navigateToSettingsGraph() {
  navigate(SETTINGS_GRAPH_ROUTE)
}
