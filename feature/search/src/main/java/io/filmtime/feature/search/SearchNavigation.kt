package io.filmtime.feature.search

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.filmtime.core.ui.navigation.DestinationRoute

val GRAPH_SEARCH_ROUTE = DestinationRoute("search_graph_route")
private const val ROUTE_SEARCH_SCREEN = "search"

fun NavGraphBuilder.searchGraph(
  onMovieClick: (rootRoute: DestinationRoute, tmdbId: Int) -> Unit,
  onShowClick: (rootRoute: DestinationRoute, tmdbId: Int) -> Unit,
  nestedGraphs: NavGraphBuilder.(DestinationRoute) -> Unit,
) {
  navigation(
    route = GRAPH_SEARCH_ROUTE.route,
    startDestination = "${GRAPH_SEARCH_ROUTE.route}/$ROUTE_SEARCH_SCREEN",
  ) {
    composable("${GRAPH_SEARCH_ROUTE.route}/$ROUTE_SEARCH_SCREEN") {
      SearchScreen(
        viewModel = hiltViewModel(),
        onMovieClick = { onMovieClick(GRAPH_SEARCH_ROUTE, it) },
        onShowClick = { onShowClick(GRAPH_SEARCH_ROUTE, it) },
        onPersonClick = {},
      )
    }
    nestedGraphs(GRAPH_SEARCH_ROUTE)
  }
}
