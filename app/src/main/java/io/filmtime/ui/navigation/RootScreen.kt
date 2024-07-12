package io.filmtime.ui.navigation

import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.feature.home.GRAPH_HOME_ROUTE
import io.filmtime.feature.movies.GRAPH_MOVIES_ROUTE
import io.filmtime.feature.settings.GRAPH_SETTINGS_ROUTE
import io.filmtime.feature.shows.GRAPH_SHOWS_ROUTE

sealed class RootScreen(val destinationRoute: DestinationRoute) {
  data object Home : RootScreen(GRAPH_HOME_ROUTE)
  data object Movies : RootScreen(GRAPH_MOVIES_ROUTE)
  data object Shows : RootScreen(GRAPH_SHOWS_ROUTE)
  data object Settings : RootScreen(GRAPH_SETTINGS_ROUTE)
}
