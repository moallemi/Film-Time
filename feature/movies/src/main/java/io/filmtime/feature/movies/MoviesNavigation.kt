package io.filmtime.feature.movies

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.core.ui.navigation.composable
import io.filmtime.data.model.VideoListType

val GRAPH_MOVIES_ROUTE = DestinationRoute("movies_graph_route")
private const val ROUTE_MOVIES_SCREEN = "movies"

fun NavGraphBuilder.moviesGraph(
  onMovieClick: (rootRoute: DestinationRoute, tmdbId: Int) -> Unit,
  onSectionClick: (rootRoute: DestinationRoute, videoListType: VideoListType) -> Unit,
  nestedGraphs: NavGraphBuilder.(DestinationRoute) -> Unit,
) {
  navigation(
    route = GRAPH_MOVIES_ROUTE.route,
    startDestination = "${GRAPH_MOVIES_ROUTE.route}/$ROUTE_MOVIES_SCREEN",
  ) {
    composable(
      route = "${GRAPH_MOVIES_ROUTE.route}/$ROUTE_MOVIES_SCREEN",
      screenName = "Movies",
    ) {
      MoviesScreen(
        onMovieClick = { onMovieClick(GRAPH_MOVIES_ROUTE, it) },
        onSectionClick = { onSectionClick(GRAPH_MOVIES_ROUTE, it) },
      )
    }

    nestedGraphs(GRAPH_MOVIES_ROUTE)
  }
}
