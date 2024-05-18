package io.filmtime.feature.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.filmtime.core.ui.common.DestinationRoute

val GRAPH_SEARCH_ROUTE = DestinationRoute("search_graph_route")
private const val ROUTE_SEARCH_SCREEN = "search"

fun NavGraphBuilder.searchGraph(
  nestedGraphs: NavGraphBuilder.(DestinationRoute) -> Unit,
) {
  navigation(
    route = GRAPH_SEARCH_ROUTE.route,
    startDestination = "${GRAPH_SEARCH_ROUTE.route}/$ROUTE_SEARCH_SCREEN",
  ) {
    composable("${GRAPH_SEARCH_ROUTE.route}/$ROUTE_SEARCH_SCREEN") {
      SearchScreen()
    }
    nestedGraphs(GRAPH_SEARCH_ROUTE)
  }
}
