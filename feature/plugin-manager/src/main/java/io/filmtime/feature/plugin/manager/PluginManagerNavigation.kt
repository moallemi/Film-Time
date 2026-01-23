package io.filmtime.feature.plugin.manager

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable

private const val ROUTE_PLUGIN_MANAGER = "plugin_manager"

fun NavGraphBuilder.pluginManagerScreen(
  rootRoute: DestinationRoute,
  onBackClick: () -> Unit,
) {
  composable(
    route = "${rootRoute.route}/$ROUTE_PLUGIN_MANAGER",
    screenName = "Plugin Manager",
  ) {
    PluginManagerScreen(
      onBackClick = onBackClick,
    )
  }
}

fun NavController.navigateToPluginManager(rootRoute: DestinationRoute) {
  navigate("${rootRoute.route}/$ROUTE_PLUGIN_MANAGER")
}
