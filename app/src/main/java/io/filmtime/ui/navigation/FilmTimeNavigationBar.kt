package io.filmtime.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import io.filmtime.R

@Composable
fun FilmTimeNavigationBar(
  destinations: List<RootDestination>,
  currentDestination: NavDestination?,
  onNavigationSelected: (RootScreen) -> Unit,
  modifier: Modifier = Modifier,
) {
  NavigationBar(
    modifier = modifier,
  ) {
    destinations.forEach { destination ->
      val selected = currentDestination.isRootDestination(destination)

      NavigationBarItem(
        selected = selected,
        onClick = { onNavigationSelected(destination.rootScreen) },
        icon = {
          Icon(
            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
            contentDescription = "${destination.title} icon",
          )
        },
        label = {
          Text(
            text = stringResource(destination.title),
          )
        },
      )
    }
  }
}

enum class RootDestination(
  val rootScreen: RootScreen,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val title: Int,
) {
  Home(
    rootScreen = RootScreen.Home,
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Outlined.Home,
    title = R.string.home,
  ),
  Movies(
    rootScreen = RootScreen.Movies,
    selectedIcon = Icons.Rounded.Movie,
    unselectedIcon = Icons.Outlined.Movie,
    title = R.string.movies,
  ),
  Shows(
    rootScreen = RootScreen.Shows,
    selectedIcon = Icons.Rounded.Subscriptions,
    unselectedIcon = Icons.Outlined.Subscriptions,
    title = R.string.series,
  ),
  Settings(
    rootScreen = RootScreen.Settings,
    selectedIcon = Icons.Rounded.Settings,
    unselectedIcon = Icons.Outlined.Settings,
    title = R.string.settings,
  ),
}

private fun NavDestination?.isRootDestination(destination: RootDestination) =
  this?.hierarchy?.any { it.route == destination.rootScreen.destinationRoute } ?: false
