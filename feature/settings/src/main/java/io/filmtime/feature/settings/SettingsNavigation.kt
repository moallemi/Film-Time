package io.filmtime.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable

val GRAPH_SETTINGS_ROUTE = DestinationRoute("settings_graph_route")
private const val ROUTE_SETTINGS_SCREEN = "settings"

fun NavGraphBuilder.settingsGraph(
  onTraktLoginClick: () -> Unit,
  nestedGraphs: NavGraphBuilder.(DestinationRoute) -> Unit,
) {
  navigation(
    route = GRAPH_SETTINGS_ROUTE.route,
    startDestination = "${GRAPH_SETTINGS_ROUTE.route}/$ROUTE_SETTINGS_SCREEN",
  ) {
    composable(
      route = "${GRAPH_SETTINGS_ROUTE.route}/$ROUTE_SETTINGS_SCREEN",
      screenName = "Settings",
    ) {
      SettingsScreen(
        onTraktLoginClick = { onTraktLoginClick() },
      )
    }

    nestedGraphs(GRAPH_SETTINGS_ROUTE)
  }
}
