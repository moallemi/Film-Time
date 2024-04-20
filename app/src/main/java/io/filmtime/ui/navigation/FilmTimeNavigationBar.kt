package io.filmtime.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

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

private fun NavDestination?.isRootDestination(destination: RootDestination) =
  this?.hierarchy?.any { it.route == destination.rootScreen.destinationRoute.route } ?: false
