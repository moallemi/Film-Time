package io.filmtime.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.filmtime.core.ui.common.DestinationRoute

val GRAPH_HOME_ROUTE = DestinationRoute("home_graph_route")
private const val ROUTE_HOME_SCREEN = "home"

fun NavGraphBuilder.homeGraph(
  onMovieClick: (rootRoute: DestinationRoute, tmdbId: Int) -> Unit,
  onShowClick: (rootRoute: DestinationRoute, tmdbId: Int) -> Unit,
  onTraktClick: () -> Unit,
  onTrendingMoviesClick: (rootRoute: DestinationRoute) -> Unit,
  onTrendingShowsClick: (rootRoute: DestinationRoute) -> Unit,
  nestedGraphs: NavGraphBuilder.(DestinationRoute) -> Unit,
) {
  navigation(
    route = GRAPH_HOME_ROUTE.route,
    startDestination = "${GRAPH_HOME_ROUTE.route}/$ROUTE_HOME_SCREEN",
  ) {
    composable(
      route = "${GRAPH_HOME_ROUTE.route}/$ROUTE_HOME_SCREEN",
    ) {
      HomeScreen(
        onMovieClick = { onMovieClick(GRAPH_HOME_ROUTE, it) },
        onShowClick = { onShowClick(GRAPH_HOME_ROUTE, it) },
        onTraktClick = onTraktClick,
        onTrendingMoviesClick = { onTrendingMoviesClick(GRAPH_HOME_ROUTE) },
        onTrendingShowsClick = { onTrendingShowsClick(GRAPH_HOME_ROUTE) },
      )
    }

    nestedGraphs(GRAPH_HOME_ROUTE)
  }
}
