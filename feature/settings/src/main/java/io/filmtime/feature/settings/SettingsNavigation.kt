package io.filmtime.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable
import io.filmtime.feature.plugin.manager.navigateToPluginManager
import io.filmtime.feature.plugin.manager.pluginManagerScreen

val GRAPH_SETTINGS_ROUTE = DestinationRoute("settings_graph_route")
private const val ROUTE_SETTINGS_SCREEN = "settings"

fun NavGraphBuilder.settingsGraph(
  navController: NavController,
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
        onPluginManagerClick = { navController.navigateToPluginManager(GRAPH_SETTINGS_ROUTE) },
      )
    }

    pluginManagerScreen(
      rootRoute = GRAPH_SETTINGS_ROUTE,
      onBackClick = { navController.popBackStack() },
    )

    nestedGraphs(GRAPH_SETTINGS_ROUTE)
  }
}
