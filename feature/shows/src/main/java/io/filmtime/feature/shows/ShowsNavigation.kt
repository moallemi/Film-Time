package io.filmtime.feature.shows

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.filmtime.core.ui.common.DestinationRoute
import io.filmtime.data.model.VideoListType

val GRAPH_SHOWS_ROUTE = DestinationRoute("shows_graph_route")
private const val ROUTE_SHOWS_SCREEN = "shows"

fun NavGraphBuilder.showsGraph(
  onShowClick: (rootRoute: DestinationRoute, tmdbId: Int) -> Unit,
  onSectionClick: (rootRoute: DestinationRoute, videoListType: VideoListType) -> Unit,
  nestedGraphs: NavGraphBuilder.(DestinationRoute) -> Unit,
) {
  navigation(
    route = GRAPH_SHOWS_ROUTE.route,
    startDestination = "${GRAPH_SHOWS_ROUTE.route}/$ROUTE_SHOWS_SCREEN",
  ) {
    composable(
      route = "${GRAPH_SHOWS_ROUTE.route}/$ROUTE_SHOWS_SCREEN",
    ) {
      ShowsScreen(
        onShowClick = { onShowClick(GRAPH_SHOWS_ROUTE, it) },
        onSectionClick = { onSectionClick(GRAPH_SHOWS_ROUTE, it) },
      )
    }

    nestedGraphs(GRAPH_SHOWS_ROUTE)
  }
}
