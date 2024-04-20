package io.filmtime.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.filmtime.core.ui.common.DestinationRoute

val GRAPH_SETTINGS_ROUTE = DestinationRoute("settings_graph_route")
private const val ROUTE_SETTINGS_SCREEN = "settings"

fun NavGraphBuilder.settingsGraph() {
  navigation(
    route = GRAPH_SETTINGS_ROUTE.route,
    startDestination = "${GRAPH_SETTINGS_ROUTE.route}/$ROUTE_SETTINGS_SCREEN",
  ) {
    composable(
      route = "${GRAPH_SETTINGS_ROUTE.route}/$ROUTE_SETTINGS_SCREEN",
    ) {
      SettingsScreen()
    }
  }
}
