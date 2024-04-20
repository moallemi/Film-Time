package io.filmtime.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import io.filmtime.ui.navigation.FilmTimeNavHost
import io.filmtime.ui.navigation.FilmTimeNavigationBar
import io.filmtime.ui.navigation.RootDestination
import io.filmtime.ui.navigation.RootScreen.Home
import io.filmtime.ui.navigation.RootScreen.Movies
import io.filmtime.ui.navigation.RootScreen.Settings
import io.filmtime.ui.navigation.RootScreen.Shows

@Composable
fun FilmTimeApp(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    bottomBar = {
      FilmTimeNavigationBar(
        destinations = RootDestination.entries,
        currentDestination = navController.currentBackStackEntryAsState().value?.destination,
        onNavigationSelected = { destination ->
          val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
              saveState = true
            }
            launchSingleTop = true
            restoreState = true
          }

          when (destination) {
            Home -> navController.navigate(Home.destinationRoute.route, topLevelNavOptions)
            Movies -> navController.navigate(Movies.destinationRoute.route, topLevelNavOptions)
            Shows -> navController.navigate(Shows.destinationRoute.route, topLevelNavOptions)
            Settings -> navController.navigate(Settings.destinationRoute.route, topLevelNavOptions)
          }
        },
      )
    },
  ) { padding ->
    FilmTimeNavHost(
      navController = navController,
    )
  }
}
